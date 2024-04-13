package godeck.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the mythology of a card.
 */
public class Mythology {

    public static final Map<String, Integer> TO_INT;
    static {
        TO_INT = new HashMap<String, Integer>();
        TO_INT.put("Greek", 0);
        TO_INT.put("Maya", 1);
        TO_INT.put("Norse", 2);
        TO_INT.put("Egyptian", 3);
        TO_INT.put("Brazilian", 4);
        TO_INT.put("Chinese", 5);
        TO_INT.put("Myth", 6);
    }

    public static final Map<Integer, String> TO_STRING;
    static {
        TO_STRING = new HashMap<Integer, String>();
        TO_STRING.put(0, "Greek");
        TO_STRING.put(1, "Maya");
        TO_STRING.put(2, "Norse");
        TO_STRING.put(3, "Egyptian");
        TO_STRING.put(4, "Brazilian");
        TO_STRING.put(5, "Chinese");
        TO_STRING.put(6, "Myth");
    }

}