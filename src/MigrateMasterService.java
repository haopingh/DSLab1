import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	    DataInputStream in = new DataInputStream(mSocket.getInputStream());
	    DataOutputStream out = new DataOutputStream(mSocket.getOutputStream());

	    while (true) {
		if (in.read() != -1) {

		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
