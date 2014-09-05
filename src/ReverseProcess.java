import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;


public class ReverseProcess extends MigratableProcess{


	public ReverseProcess(String args[]) throws Exception {
		if (args.length != 2) {
			System.out.println("usage: ReverseProcess <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		inFile = new TransactionalFileInputStream(args[0]);
		outFile = new TransactionalFileOutputStream(args[1]);
	}

	
	@Override
	public void run() {
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

		try {
			while (!suspending) {
				String line = in.readLine();
				
				if (line == null)
					break;
				
				out.println(new StringBuilder(line).reverse().toString());
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// ignore it
				}
			}
		} catch (EOFException e) {
			// End of File
		} catch (IOException e) {
			System.out.println("GrepProcess: Error: " + e);
		}
		out.close();
		suspending = false;
	}

}
