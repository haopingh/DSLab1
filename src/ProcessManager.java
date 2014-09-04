import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ProcessManager {

    public static void main(String[] args) throws Exception{

	// listen to the system in
	while (true) {
	    
	    String command = readCommand();
	    if (command.equals("migrate")) {// run migrate method
		migrate();
	    } else if (command.equals("exit")) {
		System.exit(0);
	    } else {//instantiate an object
		String[] commandArr = command.split(" ");
		String[] argsArr = Arrays.copyOfRange(commandArr, 1, commandArr.length);
		Class <?> myClass = Class.forName(commandArr[0]);
		Constructor <?> myCons = myClass.getConstructor(String[].class);
		Object object = myCons.newInstance((Object)argsArr);
		//Object object = myCons.newInstance(argsArr); // this does not work but dunno why
		Thread t = new Thread ((Runnable) object);
		t.start();
	    }
	}
    }

    // read command at runtime
    public static String readCommand() throws Exception {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String a = br.readLine();
	return a;
    }

    // migrate method
    public static void migrate() {
	// TODO should figure out how to set the flag in
	// TransactionFileInput/OutpuStream
    }

}
