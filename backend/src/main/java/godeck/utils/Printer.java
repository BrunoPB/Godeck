package godeck.utils;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import godeck.enums.TextFormatting;

/**
 * Static class that handles printing messages to the console. It can print
 * messages of different types, such as info, warning, error and debug. It also
 * prints the time, the file trace and the thread that called the print method.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class Printer {
    // Private Methods

    /**
     * Formats the message that indicates the type of the message.
     * 
     * @param type  The type of the message.
     * @param color The color of the message.
     * @return The formatted message.
     */
    private static String formatTypeMessage(String type, String color) {
        return ("[" + color + type.toUpperCase() + TextFormatting.RESET + "]");
    }

    /**
     * Formats the message that indicates the when the message was printed. It
     * uses the format "yyyy-MM-dd HH:mm:ss".
     * 
     * @return The formatted message.
     */
    private static String formatTimeMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = java.time.LocalDateTime.now().format(formatter);
        return (TextFormatting.COLOR_PURPLE + time + TextFormatting.RESET);
    }

    /**
     * Formats the message that indicates the stack trace of the message.
     * 
     * @return The formatted message.
     */
    private static String formatStackTrace() {
        return formatStackTrace(4);
    }

    /**
     * Formats the message that indicates the stack trace of the message. It uses
     * the depth to get the correct stack trace.
     * 
     * @param depth The depth of the stack trace.
     * @return The formatted message.
     */
    private static String formatStackTrace(int depth) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElements[depth];
        return (TextFormatting.COLOR_BROWN + stackTraceElement.getFileName() + ":"
                + stackTraceElement.getLineNumber() + TextFormatting.RESET);
    }

    /**
     * Formats the message that indicates the thread that called the print method.
     * 
     * @return The formatted message.
     */
    private static String formatThreadMessage() {
        return (TextFormatting.COLOR_CYAN + Thread.currentThread().getName() + TextFormatting.RESET);
    }

    // Public Methods

    /**
     * Prints the start message of the server.
     */
    public static void godeckStart() {
        print(
                "\n\t .----------------. .----------------. .----------------. .----------------. .----------------. .----------------. \n"
                        + //
                        "\t| .--------------. | .--------------. | .--------------. | .--------------. | .--------------. | .--------------. |\n"
                        + //
                        "\t| |    ______    | | |     ____     | | |  ________    | | |  _________   | | |     ______   | | |  ___  ____   | |\n"
                        + //
                        "\t| |  .' ___  |   | | |   .'    `.   | | | |_   ___ `.  | | | |_   ___  |  | | |   .' ___  |  | | | |_  ||_  _|  | |\n"
                        + //
                        "\t| | / .'   \\_|   | | |  /  .--.  \\  | | |   | |   `. \\ | | |   | |_  \\_|  | | |  / .'   \\_|  | | |   | |_/ /    | |\n"
                        + //
                        "\t| | | |    ____  | | |  | |    | |  | | |   | |    | | | | |   |  _|  _   | | |  | |         | | |   |  __'.    | |\n"
                        + //
                        "\t| | \\ `.___]  _| | | |  \\  `--'  /  | | |  _| |___.' / | | |  _| |___/ |  | | |  \\ `.___.'\\  | | |  _| |  \\ \\_  | |\n"
                        + //
                        "\t| |  `._____.'   | | |   `.____.'   | | | |________.'  | | | |_________|  | | |   `._____.'  | | | |____||____| | |\n"
                        + //
                        "\t| |              | | |              | | |              | | |              | | |              | | |              | |\n"
                        + //
                        "\t| '--------------' | '--------------' | '--------------' | '--------------' | '--------------' | '--------------' |\n"
                        + //
                        "\t '----------------' '----------------' '----------------' '----------------' '----------------' '----------------' \n");
    }

    /**
     * Prints a message to the console.
     * 
     * @param message The message to be printed.
     */
    public static void print(String message) {
        System.out.println(message);
    }

    /**
     * Prints a message to the console.
     * 
     * @param number The number to be printed.
     */
    public static void print(int number) {
        System.out.println(number);
    }

    /**
     * Prints a message to the console.
     * 
     * @param bool The boolean to be printed.
     */
    public static void print(boolean bool) {
        System.out.println(bool);
    }

    /**
     * Prints a message to the console. The message is formatted as an information
     * message.
     * 
     * @param message The message to be printed.
     */
    public static void printInfo(String message) {
        String type = formatTypeMessage("info", TextFormatting.COLOR_BLUE);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + message);
    }

    /**
     * Prints a message to the console. The message is formatted as an information
     * message.
     * 
     * @param number The number to be printed.
     */
    public static void printInfo(int number) {
        String type = formatTypeMessage("info", TextFormatting.COLOR_BLUE);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + number);
    }

    /**
     * Prints a message to the console. The message is formatted as an information
     * message.
     * 
     * @param bool The boolean to be printed.
     */
    public static void printInfo(boolean bool) {
        String type = formatTypeMessage("info", TextFormatting.COLOR_BLUE);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + bool);
    }

    /**
     * Prints a message to the console. The message is formatted as a warning
     * message.
     * 
     * @param message The message to be printed.
     */
    public static void printWarn(String message) {
        String type = formatTypeMessage("warn", TextFormatting.COLOR_BROWN);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + message);
    }

    /**
     * Prints a message to the console. The message is formatted as a warning
     * message.
     * 
     * @param number The number to be printed.
     */
    public static void printWarn(int number) {
        String type = formatTypeMessage("warn", TextFormatting.COLOR_BROWN);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + number);
    }

    /**
     * Prints a message to the console. The message is formatted as a warning
     * message.
     * 
     * @param bool The boolean to be printed.
     */
    public static void printWarn(boolean bool) {
        String type = formatTypeMessage("warn", TextFormatting.COLOR_BROWN);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + bool);
    }

    /**
     * Prints a message to the console. The message is formatted as an error
     * message.
     * 
     * @param message The message to be printed.
     */
    public static void printError(String message) {
        String type = formatTypeMessage("error", TextFormatting.COLOR_RED);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + message);
        print("\t{" + TextFormatting.COLOR_RED + "Thread" + TextFormatting.RESET + "} " + formatThreadMessage());
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        stackTrace = java.util.Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
        print("\t{" + TextFormatting.COLOR_RED + "Stack Trace" + TextFormatting.RESET + "}");
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.toString().substring(0, 6).equals("godeck")) {
                print("\t\t\t" + TextFormatting.UNDERLINE_RED + "at " + stackTraceElement + TextFormatting.RESET);
            } else {
                print("\t\t\tat " + stackTraceElement);
            }
        }
    }

    /**
     * Prints a message to the console. The message is formatted as an error
     * message.
     * 
     * @param number The number to be printed.
     */
    public static void printError(int number) {
        String type = formatTypeMessage("error", TextFormatting.COLOR_RED);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + number);
        print("\t{" + TextFormatting.COLOR_RED + "Thread" + TextFormatting.RESET + "} " + formatThreadMessage());
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        stackTrace = java.util.Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
        print("\t{" + TextFormatting.COLOR_RED + "Stack Trace" + TextFormatting.RESET + "}");
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.toString().substring(0, 6).equals("godeck")) {
                print("\t\t\t" + TextFormatting.UNDERLINE_RED + "at " + stackTraceElement + TextFormatting.RESET);
            } else {
                print("\t\t\tat " + stackTraceElement);
            }
        }
    }

    /**
     * Prints a message to the console. The message is formatted as an error
     * message.
     * 
     * @param bool The boolean to be printed.
     */
    public static void printError(boolean bool) {
        String type = formatTypeMessage("error", TextFormatting.COLOR_RED);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + "\tat " + time + " from " + fileTrace + "\t\t: " + bool);
        print("\t{" + TextFormatting.COLOR_RED + "Thread" + TextFormatting.RESET + "} " + formatThreadMessage());
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        stackTrace = java.util.Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
        print("\t{" + TextFormatting.COLOR_RED + "Stack Trace" + TextFormatting.RESET + "}");
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.toString().substring(0, 6).equals("godeck")) {
                print("\t\t\t" + TextFormatting.UNDERLINE_RED + "at " + stackTraceElement + TextFormatting.RESET);
            } else {
                print("\t\t\tat " + stackTraceElement);
            }
        }
    }

    /**
     * Prints a message to the console. The message is formatted as a debug message.
     * 
     * @param message The message to be printed.
     */
    public static void printDebug(String message) {
        String type = formatTypeMessage("debug", TextFormatting.COLOR_GREEN);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + " at " + time + " from " + fileTrace + "\t\t: " + message);
    }

    /**
     * Prints a message to the console. The message is formatted as a debug message.
     * 
     * @param number The number to be printed.
     */
    public static void printDebug(int number) {
        String type = formatTypeMessage("debug", TextFormatting.COLOR_GREEN);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + " at " + time + " from " + fileTrace + "\t\t: " + number);
    }

    /**
     * Prints a message to the console. The message is formatted as a debug message.
     * 
     * @param bool The boolean to be printed.
     */
    public static void printDebug(boolean bool) {
        String type = formatTypeMessage("debug", TextFormatting.COLOR_GREEN);
        String time = formatTimeMessage();
        String fileTrace = formatStackTrace();
        print(type + " at " + time + " from " + fileTrace + "\t\t: " + bool);
    }
}
