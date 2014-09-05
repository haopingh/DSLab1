import java.io.Serializable;

/*public interface MigratableProcess extends Runnable, Serializable {

    void suspend();
}*/

public abstract class MigratableProcess implements Runnable, Serializable {
	
	protected volatile boolean suspending;
	protected TransactionalFileInputStream inFile;
    protected TransactionalFileOutputStream outFile;
	
	public void suspend() {
		suspending = true;
		while (suspending);
	}
	
} 


