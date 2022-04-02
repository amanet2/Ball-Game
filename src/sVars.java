import java.util.HashMap;

public class sVars {
    private static HashMap<String, String> keys = null;

    private static void init() {
    }

    public static String get(String s) {
        refresh();
        return keys.get(s);
    }

    public static int getInt(String s) {
        return Integer.parseInt(get(s));
    }

    public static double getDouble(String s) {
        return Double.parseDouble(get(s));
    }

    public static void put(String s, String v) {
        refresh();
        keys.put(s,v);
    }

    public static void putInt(String s, int v) {
        put(s, Integer.toString(v));
    }

    public static void putLong(String s, long v) {
        put(s, Long.toString(v));
    }

    public static HashMap<String, String> vars() {
        refresh();
        return keys;
    }

    public static boolean contains(String s) {
        return keys.containsKey(s);
    }

    private static void refresh() {
        if(keys == null) {
            keys = new HashMap<>();
            init();
        }
    }

    public static void remove(String s) {
        refresh();
        keys.remove(s);
    }
}
