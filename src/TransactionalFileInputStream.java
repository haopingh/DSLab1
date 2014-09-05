import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class TransactionalFileInputStream extends InputStream implements
	Serializable {

    private int offset;
    private String filename;

    /* "Cache" the connection in FileInputStream */
    private transient FileInputStream mInput;

    public TransactionalFileInputStream(String fname) {
    	offset = 0;
    	filename = fname;

    	try {
    	    mInput = new FileInputStream(filename);
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	}
    }

    @Override
    public int read() throws IOException {
    	if (mInput == null) {
    		System.out.println("FileInputStream reset! Start new one, with offset: " + offset);
    		mInput = new FileInputStream(filename);
    		mInput.skip(offset);
    	}
	
    	offset++;
    	return mInput.read();
    }

}
