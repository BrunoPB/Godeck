package godeck.models;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

/**
 * Godeck's custom Thread model class. Extends the Java Thread class and adds
 * the ability to kill the thread. It also has a CompletableFuture object that
 * will be completed when the thread is killed.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public abstract class GodeckThread extends Thread {
    // Properties

    protected boolean exit = false;
    public CompletableFuture<Void> killed = new CompletableFuture<Void>();

    // Constructors

    /**
     * Creates a new GodeckThread.
     */
    public GodeckThread() {
    }

    /**
     * Runs the thread.
     */
    abstract public void run();

    /**
     * Kills the thread.
     */
    public void kill() {
        this.exit = true;
        this.killed.complete(null);
    }
}
