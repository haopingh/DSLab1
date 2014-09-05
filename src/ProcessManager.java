import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessManager {
	/* Server Socket, always waiting for other's connection */
	private MigrateMatser mServer;
	
	/* Port for Listening other connection */
	private static final int port = 5566;
	
	/* 
	 * Maintain other nodes address (Hard Coded) 
	 * 187 -> ghc54, 188 -> ghc55
	 */
	private String[] nodeIP = {"128.2.100.187", "128.2.100.188"};
	
	/* Data Structure to store existing threads */
	ArrayList<MigratableProcess> threads;
	
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
		threads = new ArrayList<MigratableProcess>();
	}
	
	public void addAndStart(MigratableProcess mp) {
		threads.add(mp);
		Thread t = new Thread(mp);
		t.start();
	}
	
	// migrate method
    public void migrate() {
	// TODO should figure out how to set the flag in
	// TransactionFileInput/OutpuStream
    }
	
	
	
	
	/*
	 * Here Start the Main Program
	 * The main program has a ProcessManager object to do connection, create process, migrate process...
	 */
    public static void main(String[] args) throws Exception{

    	/* Create ProcessManager object to handle different commands */
    	ProcessManager mManager = new ProcessManager();
    	
    	// listen to the system in
    	while (true) {
    	    
    	    String command = readCommand();
    	    if (command.equals("migrate")) {// run migrate method
    	    	mManager.migrate();
    	    } else if (command.equals("exit")) {
    	    	System.exit(0);
    	    } else {//instantiate an object
    	    	String[] commandArr = command.split(" ");
    	    	String[] argsArr = Arrays.copyOfRange(commandArr, 1, commandArr.length);
    	    	Class <?> myClass = Class.forName(commandArr[0]);
    	    	Constructor <?> myCons = myClass.getConstructor(String[].class);
    	    	Object object = myCons.newInstance((Object)argsArr);
    		
    	    	mManager.addAndStart((MigratableProcess)object);
    	    }
    	}
    }

    // read command at runtime
    public static String readCommand() throws Exception {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String a = br.readLine();
	return a;
    }

}
