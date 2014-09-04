import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ProcessManager {
  
    public static void main (String[] args) throws IOException {
        //listen to the system in
        while (true) {
            String command = readCommand();
            if (command.equals("migrate")) {// run migrate method
                migrate();
            }
            else if (command.equals("exit")) {
                System.exit(0);
            }
            else {// 
                String[] commandArr = command.split(" ");
                
            }
        }
    }
    
    //read command at runtime
    public static String readCommand () throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        String a = br.readLine();
        return a;
    }
    
    //migrate method
    public static void migrate () { 
        //TODO should figure out how to set the flag in TransactionFileInput/OutpuStream
    }
    
    
}
