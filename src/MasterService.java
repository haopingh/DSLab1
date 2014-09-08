import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MasterService implements Runnable {
    private Socket mSocket;

    public MasterService(Socket s) {
    	mSocket = s;
    }

    @Override
    public void run() {
    	
	try {

	    ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
	    
	    while (true) {
	    	Object migratedObj = in.readObject();
	    	
	    	if (migratedObj != null) {
	    		if (migratedObj instanceof MigratableProcess) {
	    			System.out.println("Received process successfully");
	    			System.out.println("Process running!");
	    			
	    			Thread t = new Thread((MigratableProcess)migratedObj);
		    		t.start();
		    		
		    		t.join();
		    		ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());
		    		out.writeObject("Finished running"); //send a response to master node
		    		System.out.println("Finished running!");
		    		out.flush();
	    		}
	    	}
	    }

	}
	catch(IOException e) {
		System.out.print(e);
	}
	catch (Exception e) {
		System.out.println(e);
	    e.printStackTrace();
	}
    }
}
