import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MigrateClient implements Runnable {
    private Socket mSocket;
    private MigratableProcess mp;
    
    private ThreadFinishListener mListener;

    public MigrateClient(Socket s) {
    	mSocket = s;
    }

    @Override
    public void run() {
    	
    	try {
    	    ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());

    	    System.out.println("MigrateClient: Start Transmission");
    	    if (mp != null) {
    	    	out.writeObject((Object)mp);
    	    	out.flush();
    	    	System.out.println("MigrateClient: Finish Transmission");
    	    	
    	    	ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
    	    	while(true){
    	    		Object response = in.readObject();
    	    		if (response instanceof String && ((String)response).equals("OK")) {
    	    			System.out.println("Get Process Response: OK");
    	    			mListener.onThreadFinish(mp);
    	    			break;
    	    		}
    	    	}
    	    }


    	} catch (Exception e) {
    	    e.printStackTrace();
    	}


    }
    
    public void setTransmitProcess(MigratableProcess m) {
    	mp = m;
    }

    public interface ThreadFinishListener {
    	void onThreadFinish(MigratableProcess mp);
    }
    
    public void setListener(ThreadFinishListener l) { mListener = l; }
}
