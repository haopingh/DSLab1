import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MigrateMasterService implements Runnable {
    private Socket mSocket;

    public MigrateMasterService(Socket s) {
	mSocket = s;
    }

    @Override
    public void run() {
	try {

	    ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
	    ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());

	    while (true) {
	    	Object migratedObj = in.readObject();
	    	if (migratedObj != null) {
	    		Thread t = new Thread((MigratableProcess)migratedObj);
	    		t.start();
	    	}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
