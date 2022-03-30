import java.util.HashMap;

public class cVars {
    //vars for client
    private static HashMap<String, String> vars = null;

    private static void init() {
        put("maploaded", "0");
    }

    public static String get(String s) {
        refresh();
        return vars.get(s);
    }
    public static int getInt(String s) {
        return Integer.parseInt(get(s));
    }
    public static double getDouble(String s) {
        return Double.parseDouble(get(s));
    }
    public static void put(String s, String v) {
        refresh();
        vars.put(s,v);
    }
    public static void putDouble(String s, double v) {
        put(s, Double.toString(v));
    }
    public static void putInt(String s, int v) {
        put(s, Integer.toString(v));
    }
    public static void putLong(String s, long v) {
        put(s, Long.toString(v));
    }
    public static boolean isVal(String s, String v) {
        return get(s).equals(v);
    }
    public static boolean isInt(String s, int v) {
        return isVal(s, Integer.toString(v));
    }
    public static boolean isOne(String s) {
        return get(s).equals("1");
    }
    public static boolean isZero(String s) {
        return get(s).equals("0");
    }
    public static HashMap<String, String> vars() {
        refresh();
        return vars;
    }
    public static boolean contains(String s) {
        refresh();
        return vars.containsKey(s);
    }
    public static void remove(String s) {
        refresh();
        vars.remove(s);
    }
    private static void refresh() {
        if(vars == null) {
            vars = new HashMap<>();
            init();
        }
    }
}
