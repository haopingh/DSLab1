import java.io.PrintStream;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;

public class GrepProcess extends MigratableProcess {

	private String query;

	public GrepProcess(String args[]) throws Exception {
		if (args.length != 3) {
			System.out.println("usage: GrepProcess <queryString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		query = args[0];
		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2]);
	}

	public void run() {
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

		try {
			while (!suspending) {
				
				String line = in.readLine();
				
				if (line == null)
					break;
				
				if (line.contains(query)) {
					out.println(line);
				}
				// Make grep take longer so that we don't require extremely
				// large files for interesting results
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
		suspending = false;
	}

}