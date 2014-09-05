import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/*
 *  TODO: Sync between write file and migrate thread.   by haoping
 */
public class TransactionalFileOutputStream extends OutputStream implements Serializable {

    private int offset;
    private String filename;

    /* "Cache" the connection in FileInputStream */
    private transient FileOutputStream mOutput;

    public TransactionalFileOutputStream(String fname) {
    	offset = 0;
    	filename = fname;

    	try {
    	    mOutput = new FileOutputStream(filename, true);
    	} catch (FileNotFoundException e) {
    	    // TODO Auto-generated catch block
    	    e.printStackTrace();
    	}
    }

    @Override
    public void write(int arg0) throws IOException {
    	
    	if (mOutput == null) {
    		mOutput = new FileOutputStream(filename);
    	}
    	mOutput.write(arg0);

    	offset++;
    }

}
