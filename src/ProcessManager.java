import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ProcessManager implements Client.ThreadFinishListener{
	

	//port used to listen to connections 
	private static final int port = 5566;

	/*
	 * Hard-coded node's IP address
	 * 128.2.100.187 -> ghc54
	 * 128.2.100.188 -> ghc55 (node 0)
	 * 128.2.100.189 -> ghc56 (node 1)
	 */
	private String[] nodeIP = { "128.2.100.187", "128.2.100.188", "128.2.100.189" };
	private int processNum;
	
	//store existing threads and their corresponding numbers
	ArrayList<MigratableProcess> mpObj;
	ArrayList<MigratableProcess> allObj;
	ArrayList<Integer> migraObj;
	int [] nodeusage;
	HashMap <MigratableProcess, Integer> mp2ip;
	
	public ProcessManager() {
		//Bind port to this program, so other nodes can connect to here 
		try {
			ServerSocket mServer = new ServerSocket(port); //server
			Matser mMaster = new Matser(mServer);
			Thread t = new Thread(mMaster);  
			t.start(); //keep listening to this port

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Initialize threads management elements
		mpObj = new ArrayList<MigratableProcess>();
		allObj = new ArrayList<MigratableProcess>();
		migraObj = new ArrayList<Integer>();
	
		nodeusage = new int [2];
		mp2ip = new HashMap <MigratableProcess, Integer>();
		processNum = 0;
	}

	public void launch(MigratableProcess mp) {
		mpObj.add(mp);
		allObj.add(mp);
		migraObj.add(processNum);
		processNum++;
		Thread t = new Thread(mp);
		t.start();
	}

	// migrate method
	public void migrate() {
		System.out.println("Choose which process you want to migrate:");
		
		for (int i = 0; i < migraObj.size(); i++)
			System.out.println(migraObj.get(i) + ": "  + mpObj.get(i).getName());
		System.out.println();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			int procNum = Integer.parseInt(br.readLine());
			
			
			MigratableProcess m = mpObj.get(migraObj.indexOf((Integer)procNum));
			mpObj.remove(m);
			migraObj.remove((Integer)procNum);
			
			int ipidx = nodeusage[0] <= nodeusage[1]? 1: 2; 
			String targetIP = nodeIP[ipidx];
			nodeusage[ipidx-1] ++;
			mp2ip.put(m, ipidx);
			
			System.out.println("Automatically migarte " + "\"" + m.getName() + "\"" +" to node:" + targetIP);
			m.suspend();
			
	        
	        Socket otherNodeSocket = new Socket(targetIP, port);
	        Client mClient = new Client(otherNodeSocket);
	        mClient.setTransmitProcess(m);

	        System.out.println("Start transmission");
	        mClient.setListener(this);

            Thread t = new Thread(mClient);
            t.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onThreadFinish(MigratableProcess mp) {
		System.out.println("Finished " + "\"" + mp.getName() + "\"" +" from node: " + nodeIP[mp2ip.get(mp)]);
		nodeusage[mp2ip.get(mp)-1] --;
		allObj.remove(mp);
		
			
		/*
		System.out.println("Current Process: ");
		for(int i = 0; i < mpObj.size(); i++) 
			System.out.print(" " + migraObj.get(i));
			*/
	}
	
	// read command at runtime
	public String readCommand() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String a = br.readLine();
		return a;
	}
	
	public void printStatus () {
		for (int i = 0; i < mpObj.size(); i++) {
			System.out.println(mpObj.get(i).getName());
		}
	}
	
	// Main program
	public static void main(String[] args) throws Exception {

		// Create ProcessManager object to handle different commands 
		ProcessManager mManager = new ProcessManager();

		while (true) {
			// listen to the system in
			String command = mManager.readCommand();
			String[] commandArr = command.split(" ");
			String[] argsArr = Arrays.copyOfRange(commandArr, 1, commandArr.length);

			if (commandArr[0].equals("migrate")) {// run migrate method
				mManager.migrate();
			} else if (commandArr[0].equals("exit")) {
				System.exit(0);
			} else if (commandArr[0].equals("ps")) {
				mManager.printStatus();
			} else {
				//TODO need to handle exception?
				// instantiate an object, using reflection in JAVA
				Class<?> myClass = Class.forName(commandArr[0]);
				Constructor<?> myCons = myClass.getConstructor(String[].class);
				Object object = myCons.newInstance((Object) argsArr);
				mManager.launch((MigratableProcess) object);
			}
		}
	}

}
