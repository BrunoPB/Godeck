package godeck.utils;

import org.springframework.stereotype.Component;

/**
 * Static class that has methods to manage threads.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class ThreadUtils {

    // Public Methods

    /**
     * Sleeps the thread for a given amount of time. It catches the exception.
     * 
     * @param time The time to sleep in milliseconds.
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            ErrorHandler.message(e);
        }
    }
}
