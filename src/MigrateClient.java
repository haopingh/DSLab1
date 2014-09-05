import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MigrateClient implements Runnable {
    private Socket mSocket;
    private MigratableProcess mp;

    public MigrateClient(Socket s) {
	mSocket = s;
    }

    @Override
    public void run() {
	try {

	    ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
	    ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());

	    if (mp != null) {
	    	out.writeObject((Object)mp);
	    	out.flush();
	    	
	    	mp = null;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void setTransmitProcess(MigratableProcess m) {
    	mp = m;
    }

}
