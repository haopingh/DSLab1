import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MigrateClient implements Runnable {
    private Socket mSocket;
    private MigratableProcess mp;

    public MigrateClient(Socket s) {
	mSocket = s;
    }

    @Override
    public void run() {
    	
	try {
	    ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());

	    System.out.println("MigrateClient:  Start Transmit");
	    if (mp != null) {
	    	out.writeObject((Object)mp);
	    	out.flush();
	    	System.out.println("MigrateClient:  Finish Transmit");
	    	//mp = null;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void setTransmitProcess(MigratableProcess m) {
    	mp = m;
    }

}
