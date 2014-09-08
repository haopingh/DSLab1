import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessManager implements Client.ThreadFinishListener{
	

	//port used to listen to connections 
	private static final int port = 5566;

	/*
	 * Hard-coded node's IP address
	 * 128.2.100.187 -> ghc54
	 * 128.2.100.188 -> ghc55
	 * 128.2.100.189 -> ghc56
	 */
	private String[] nodeIP = { "128.2.100.187", "128.2.100.188", "128.2.100.189" };
	private int processNum;
	
	//store existing threads and their corresponding numbers
	ArrayList<MigratableProcess> mpObj;
	ArrayList<Integer> migraObj;
	

	
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
		migraObj = new ArrayList<Integer>();
		processNum = 0;
	}

	public void launch(MigratableProcess mp) {
		mpObj.add(mp);
		migraObj.add(processNum); //TODO should modify this, should be able to handle adding new process after migration
		processNum++;
		Thread t = new Thread(mp);
		t.start();
		
	}

	// migrate method
	public void migrate() {
		System.out.print("Choose which process you want to migrate:");
		
		for (int i = 0; i < migraObj.size(); i++)
			System.out.print(" " + migraObj.get(i));
		System.out.println();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			int procNum = Integer.parseInt(br.readLine());
			migraObj.remove(procNum);
			MigratableProcess m = mpObj.get(procNum);

			m.suspend();
			String targetIP = nodeIP[1];
	        
	        Socket otherNodeSocket = new Socket(targetIP, port);
	        Client mClient = new Client(otherNodeSocket);
	        mClient.setTransmitProcess(m);

	        System.out.println("start transmission");
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
		System.out.println("\"Finished " + mp.getName() + "\"" +" from node:");
		
		int index = mpObj.indexOf(mp);
		migraObj.remove(index);
		mpObj.remove(mp);
		
			
		
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
				// instantiate an object, using reflection in JAVA
				Class<?> myClass = Class.forName(commandArr[0]);
				Constructor<?> myCons = myClass.getConstructor(String[].class);
				Object object = myCons.newInstance((Object) argsArr);
				mManager.launch((MigratableProcess) object);
			}
		}
	}

}
