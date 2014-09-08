import java.io.Serializable;



public abstract class MigratableProcess implements Runnable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected volatile boolean suspending;
	protected TransactionalFileInputStream inFile;
    protected TransactionalFileOutputStream outFile;
    protected String name;
    
	public void suspend() {
		suspending = true;
		while (suspending);
	}
	
	abstract String getName ();
	
} 


