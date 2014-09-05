import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MigrateClient implements Runnable {
    private Socket mSocket;

    public MigrateClient(Socket s) {
	mSocket = s;
    }

    @Override
    public void run() {
	try {

	    Scanner in = new Scanner(mSocket.getInputStream());
	    PrintWriter out = new PrintWriter(mSocket.getOutputStream());

	    while (true) {
		if (in.hasNext()) {

		    String msgIn = in.nextLine();

		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
