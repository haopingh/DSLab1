import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Matser implements Runnable {

    
    private ServerSocket mServer;

    public Matser(ServerSocket s) {
    	mServer = s;
    }

    @Override
    public void run() {

    	while (true) {
    	    try {
    		Socket clientConnect = mServer.accept();

    		System.out.println("Get connection");
    		MasterService eachConnection = new MasterService(clientConnect);

    		Thread connectionThread = new Thread(eachConnection);
    		connectionThread.start();

    	    } catch (IOException e) {
    		e.printStackTrace();
    	    }
    	}
    }  
}
