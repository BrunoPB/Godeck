package godeck.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Utility class to convert objects to JSON and vice-versa.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class JSON {
    // Properties

    private static List<Object> basicTypes = Arrays.asList(String.class, Integer.class, Double.class, Float.class,
            Boolean.class, int.class, double.class, float.class, boolean.class);

    private static List<Object> listTypes = Arrays.asList(List.class, ArrayList.class, LinkedList.class, Set.class,
            HashSet.class, TreeSet.class);

    // Private Methods

    /**
     * Constructs an object of the given type from a JSON string.
     * 
     * @param jsonString JSON string to be parsed.
     * @param type       Type of the object to be constructed.
     * @return Object constructed from the JSON string.
     */
    private static Object constructObject(String jsonString, Class<?> type) {
        try {
            Field[] fields = type.getDeclaredFields();
            JSONObject jsonObject = new JSONObject(jsonString);
            Object object = null;
            try {
                object = type.getDeclaredConstructor().newInstance();
            } catch (IndexOutOfBoundsException | SecurityException e) {
                throw new Exception("Class " + type.getName() + " must have a public constructor with no arguments.");
            }
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType.equals(String.class)) {
                    field.set(object, jsonObject.getString(field.getName()));
                } else if (fieldType.equals(UUID.class)) {
                    field.set(object, UUID.fromString(jsonObject.getString(field.getName())));
                } else if (basicTypes.contains(fieldType)) {
                    field.set(object, jsonObject.get(field.getName()));
                } else if (listTypes.contains(fieldType)) {
                    field.set(object, jsonObject.getJSONArray(field.getName()).toList());
                } else {
                    field.set(object, construct(jsonObject.getJSONObject(field.getName()).toString(), fieldType));
                }
                field.setAccessible(false);
            }
            return object;
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
        return null;
    }

    /**
     * Constructs a list of objects of the given type from a JSON string.
     * 
     * @param jsonString JSON string to be parsed.
     * @param type       Type of the objects to be constructed.
     * @return List of objects constructed from the JSON string.
     */
    private static List<?> constructList(String jsonString, Class<?> type) {
        try {
            List<Object> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (var obj : jsonArray) {
                list.add(JSON.construct(obj.toString(), type));
            }
            return list;
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
        return null;
    }

    /**
     * Converts an object to a JSON string.
     * 
     * @param object Object to be converted.
     * @return JSON string representing the object.
     */
    private static String stringifyObject(Object object) {
        try {
            Class<?> type = object.getClass();
            Field[] fields = type.getDeclaredFields();
            String json = "{";
            for (Field field : fields) {
                field.setAccessible(true);
                json += "\"" + field.getName() + "\":";
                Class<?> fieldType = field.getType();
                if (fieldType.equals(String.class)) {
                    json += "\"" + field.get(object) + "\",";
                } else if (fieldType.equals(UUID.class)) {
                    json += "\"" + field.get(object) + "\",";
                } else if (basicTypes.contains(fieldType)) {
                    json += field.get(object) + ",";
                } else if (listTypes.contains(fieldType)) {
                    json += "[";
                    List<?> list = (List<?>) field.get(object);
                    for (Object element : list) {
                        json += stringify(element) + ",";
                    }
                    json = json.substring(0, json.length() - 1);
                    json += "],";
                } else {
                    json += stringify(field.get(object)) + ",";
                }
                field.setAccessible(false);
            }
            json = json.substring(0, json.length() - 1);
            json += "}";
            return json;
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
        return "";
    }

    /**
     * Converts a list to a JSON string.
     * 
     * @param list List to be converted.
     * @return JSON string representing the list.
     */
    private static String stringifyList(List<?> list) {
        try {
            String json = "[";
            for (Object object : list) {
                json += stringify(object) + ",";
            }
            json = json.substring(0, json.length() - 1);
            json += "]";
            return json;
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
        return "";
    }

    // Public Methods

    /**
     * Constructs an object of the given type from a JSON string. Acts like a JSON
     * parser.
     * 
     * @param jsonString JSON string to be parsed.
     * @param type       Type of the object to be constructed.
     * @return Object constructed from the JSON string.
     * @throws Exception
     */
    public static Object construct(String jsonString, Class<?> type) {
        return constructObject(jsonString, type);
    }

    /**
     * Constructs an object of the given type from a JSON string. Acts like a JSON
     * parser.
     * 
     * @param jsonString JSON string to be parsed.
     * @param type       Type of the object to be constructed.
     * @param isList     If the object is a list or a set.
     * @return Object constructed from the JSON string.
     */
    public static Object construct(String jsonString, Class<?> type, boolean isList) {
        if (isList) {
            return constructList(jsonString, type);
        } else {
            return constructObject(jsonString, type);
        }
    }

    /**
     * Converts an object to a JSON string.
     * 
     * @param object Object to be converted.
     * @return JSON string representing the object.
     * @throws IllegalArgumentException If the object is not an instance of a class.
     * @throws IllegalAccessException   If the object is not accessible.
     */
    public static String stringify(Object object) {
        if (object == null) {
            return "null";
        }
        if (listTypes.contains(object.getClass())) {
            return stringifyList((List<?>) object);
        } else {
            return stringifyObject(object);
        }
    }
}
