import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessManager {
	/* Server Socket, always waiting for other's connection */
	private MigrateMatser mServer;

	/* Port for Listening other connection */
	private static final int port = 5566;

	/*
	 * Maintain other nodes address (Hard Coded) 187 -> ghc54, 188 -> ghc55
	 */
	private String[] nodeIP = { "128.2.100.187", "128.2.100.188" };

	/* Data Structure to store existing threads */
	ArrayList<MigratableProcess> mpObj;
	ArrayList<Integer> migraObj;

	public ProcessManager() {
		/* Bind Port to this program, so other node can connect to here */
		try {
			ServerSocket mServer = new ServerSocket(port);
			MigrateMatser mMaster = new MigrateMatser(this, mServer);
			Thread t = new Thread(mMaster);
			t.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Initialize threads management elements */
		mpObj = new ArrayList<MigratableProcess>();
		migraObj = new ArrayList<Integer>();
	}

	public void launch(MigratableProcess mp) {
		mpObj.add(mp);
		migraObj.add(mpObj.size() - 1);
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
			MigratableProcess m = mpObj.get(procNum);
			
			String targetIP = nodeIP[1];
	        
	        Socket otherNodeSocket = new Socket(targetIP, port);
	        MigrateClient mClient = new MigrateClient(otherNodeSocket);
	        mClient.setTransmitProcess(m);
            Thread t = new Thread(mClient);
            t.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	// read command at runtime
	public String readCommand() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String a = br.readLine();
		return a;
	}

	/*
	 * Here Start the Main Program The main program has a ProcessManager object
	 * to do connection, create process, migrate process...
	 */
	public static void main(String[] args) throws Exception {

		/* Create ProcessManager object to handle different commands */
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
			} else {// instantiate an object
				Class<?> myClass = Class.forName(commandArr[0]);
				Constructor<?> myCons = myClass.getConstructor(String[].class);
				Object object = myCons.newInstance((Object) argsArr);
				mManager.launch((MigratableProcess) object);
			}
		}
	}
}
