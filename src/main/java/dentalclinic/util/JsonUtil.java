package dentalclinic.util;

public class JsonUtil {

    /** Wraps a value in quotes and escapes characters that would break JSON. */
    public static String jsonString(String value) {
        if (value == null) return "null";
        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
        return "\"" + escaped + "\"";
    }
}