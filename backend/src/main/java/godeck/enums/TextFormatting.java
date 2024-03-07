package godeck.enums;

import org.springframework.stereotype.Component;

/**
 * Represents the text formatting for the console. It has different colors,
 * backgrounds, bold, italic and underline.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class TextFormatting {
    // Reset
    public static final String RESET = "\u001B[0m";

    // Colors
    public static final String COLOR_BLACK = "\u001B[30m";
    public static final String COLOR_RED = "\u001B[31m";
    public static final String COLOR_GREEN = "\u001B[32m";
    public static final String COLOR_BROWN = "\u001B[33m";
    public static final String COLOR_BLUE = "\u001B[34m";
    public static final String COLOR_PURPLE = "\u001B[35m";
    public static final String COLOR_CYAN = "\u001B[36m";
    public static final String COLOR_WHITE = "\u001B[37m";

    // Backgrounds
    public static final String BACKGROUND_BLACK = "\u001B[40m";
    public static final String BACKGROUND_RED = "\u001B[41m";
    public static final String BACKGROUND_GREEN = "\u001B[42m";
    public static final String BACKGROUND_YELLOW = "\u001B[43m";
    public static final String BACKGROUND_BLUE = "\u001B[44m";
    public static final String BACKGROUND_PURPLE = "\u001B[45m";
    public static final String BACKGROUND_CYAN = "\u001B[46m";
    public static final String BACKGROUND_WHITE = "\u001B[47m";

    // Underline
    public static final String UNDERLINE_BLCK = "\033[4;30m";
    public static final String UNDERLINE_RED = "\033[4;31m";
    public static final String UNDERLINE_GREEN = "\033[4;32m";
    public static final String UNDERLINE_YELLOW = "\033[4;33m";
    public static final String UNDERLINE_BLUE = "\033[4;34m";
    public static final String UNDERLINE_PURPLE = "\033[4;35m";
    public static final String UNDERLINE_CYAN = "\033[4;36m";
    public static final String UNDERLINE_WHITE = "\033[4;37m";

    // Bold
    public static final String BOLD_BLACK = "\033[1;30m";
    public static final String BOLD_RED = "\033[1;31m";
    public static final String BOLD_GREEN = "\033[1;32m";
    public static final String BOLD_YELLOW = "\033[1;33m";
    public static final String BOLD_BLUE = "\033[1;34m";
    public static final String BOLD_PURPLE = "\033[1;35m";
    public static final String BOLD_CYAN = "\033[1;36m";
    public static final String BOLD_WHITE = "\033[1;37m";

    // Italics
    public static final String ITALIC_BLACK = "\033[3;30m";
    public static final String ITALIC_RED = "\033[3;31m";
    public static final String ITALIC_GREEN = "\033[3;32m";
    public static final String ITALIC_YELLOW = "\033[3;33m";
    public static final String ITALIC_BLUE = "\033[3;34m";
    public static final String ITALIC_PURPLE = "\033[3;35m";
    public static final String ITALIC_CYAN = "\033[3;36m";
    public static final String ITALIC_WHITE = "\033[3;37m";
}
