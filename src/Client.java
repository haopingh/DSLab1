import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private Socket mSocket;
    private MigratableProcess mp;
    
    private ThreadFinishListener mListener;

    public Client(Socket s) {
    	mSocket = s;
    }

    @Override
    public void run() {
    	
    	try {
    	    ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());
    	    
    	    if (mp != null) {
    	    	out.writeObject((Object)mp);
    	    	out.flush();
    	    	System.out.println("Finish transmission");
    	    	
    	    	//wait for response from other nodes
    	    	ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
    	    	
    	    	while(true){
    	    		Object response = in.readObject();
    	    		if (response instanceof String && ((String)response).equals("Finished running")) {
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
