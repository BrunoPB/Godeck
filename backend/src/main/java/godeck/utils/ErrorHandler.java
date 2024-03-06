package godeck.utils;

import org.springframework.stereotype.Component;

/**
 * Static class that handles exceptions.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class ErrorHandler {

    // Public Methods

    /**
     * Prints the message of an exception and its stack trace.
     * 
     * @param e The exception.
     */
    public static void message(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
    }
}
