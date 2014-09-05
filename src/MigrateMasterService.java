import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    	System.out.println("Service Start Run!!");
	try {

	    ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
	    
	    while (true) {
	    	Object migratedObj = in.readObject();
	    	System.out.println("get   " + String.valueOf(migratedObj == null));
	    	if (migratedObj != null) {
	    		System.out.println("recieve object");
	    		Thread t = new Thread((MigratableProcess)migratedObj);
	    		t.start();
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
