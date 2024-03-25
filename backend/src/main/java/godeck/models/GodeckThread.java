package godeck.models;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

/**
 * Godeck's custom Thread model class. Extends the Java Thread class and adds
 * the ability to kill the thread. It also has a CompletableFuture object that
 * will be completed when the thread is killed.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
public abstract class GodeckThread extends Thread {
    // Properties

    protected boolean exit = false;
    public CompletableFuture<Void> killed = new CompletableFuture<Void>();

    // Constructors

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
