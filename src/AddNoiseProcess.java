import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;


public class AddNoiseProcess extends MigratableProcess {

	private String noise;
	
	public AddNoiseProcess(String args[]) throws Exception {
		if (args.length != 3) {
			System.out.println("usage: AddNoiseProcess <Noise> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		noise = args[0];
		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2]);
	}
	
	@Override
	public void run() {
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);
		out.println("node starts running");
		try {
			while (!suspending) {
				String line = in.readLine();
				
				if (line == null)
					break;
				
				out.println(line);
				out.println(noise);
				
				try {
					Thread.sleep(500);
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
