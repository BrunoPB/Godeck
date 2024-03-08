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
        Printer.printError(e.getMessage());
    }

    /**
     * Prints a message as an error. Also prints it's stack trace.
     * 
     * @param e The message
     */
    public static void message(String e) {
        Printer.printError(e);
    }
}
