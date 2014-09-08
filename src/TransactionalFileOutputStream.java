import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 *  TODO: Sync between write file and migrate thread.   by haoping
 */
public class TransactionalFileOutputStream extends OutputStream implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filename;

    // "Cache" the connection in FileInputStream 
    private transient FileOutputStream mOutput;

    public TransactionalFileOutputStream(String fname) {
    	filename = fname;

    	try {
    	    mOutput = new FileOutputStream(filename,true);
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	}
    }

    @Override
    public void write(int arg0) throws IOException {
    	if (mOutput == null) {
    		mOutput = new FileOutputStream(filename, true);
    	}

		mOutput = new FileOutputStream(filename, true);
    	mOutput.write(arg0);
    }
    
    @Override
	public void close () throws IOException {
    	mOutput.close();
    }

}
