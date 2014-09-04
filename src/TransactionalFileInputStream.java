import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class TransactionalFileInputStream extends InputStream implements Serializable{

	private int offset;
	private String filename;
	
	/* "Cache" the connection in FileInputStream */
	private FileInputStream mInput;
	
	/* means "safe" when no migration */
	private boolean safe = true;
	
	public TransactionalFileInputStream(String fname) {
		offset = 0;
		filename = fname;
		
		try {
			mInput = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int read() throws IOException {
		int ans;
		if (safe) {
			ans =  mInput.read();
		}
		else {
			mInput = new FileInputStream(filename);
			mInput.skip(offset);
			ans = mInput.read();
			safe = true;
		}
		
		offset++;
		return ans;
	}

}
