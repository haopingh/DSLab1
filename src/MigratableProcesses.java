import java.io.Serializable;


public interface MigratableProcesses extends Runnable, Serializable{
    void suspend ();
    String toString();
}
