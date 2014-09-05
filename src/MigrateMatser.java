import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MigrateMatser implements Runnable {

    private ProcessManager mManager;
    private ServerSocket mServer;

    public MigrateMatser(ProcessManager pm, ServerSocket s) {
    	mManager = pm;
    	mServer = s;
    }

    @Override
    public void run() {

    	while (true) {
    	    try {
    		Socket clientConnect = mServer.accept();

    		System.out.println("MigrateMaster: Get Connection");
    		
    		MigrateMasterService eachConnection = new MigrateMasterService(clientConnect);

    		Thread connectionThread = new Thread(eachConnection);
    		connectionThread.start();

    	    } catch (IOException e) {
    		e.printStackTrace();
    	    }
    	}
    }  
}
