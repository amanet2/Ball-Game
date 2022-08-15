import java.util.HashMap;

public class sVars {
    private static HashMap<String, String> keys = null;

    public static int getInt(String s) {
        refresh();
        return Integer.parseInt(keys.get(s));
    }

    public static String get(String s) {
        refresh();
        return keys.get(s);
    }

    public static void put(String s, String v) {
        refresh();
        keys.put(s,v);
    }

    private static void refresh() {
        if(keys == null) {
            keys = new HashMap<>();
        }
    }
}
