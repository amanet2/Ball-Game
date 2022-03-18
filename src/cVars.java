import java.util.HashMap;

public class cVars {
    //vars for client
    private static HashMap<String, String> vars = null;

    private static void init() {
        putInt("gamemode", cGameLogic.DEATHMATCH);
        put("maploaded", "0");
        put("newprefabname", "room");
        put("itemid", "0");
        put("prefabid", "0");
        put("selectedprefabid", "");
    }

    static boolean checkVal(String key, String v) {
        try {
            String k = key.toLowerCase();
            if(k.contains("vfx")) {
                if (v.contains("."))
                    return Double.parseDouble(v) >= 0 && Double.parseDouble(v) < 256;
                return Integer.parseInt(v) >= 0 && Integer.parseInt(v) < 256;
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
            return false;
        }
        return true;
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
    public static long getLong(String s) {
        return Long.parseLong(get(s));
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
    public static void addIntVal(String k, int v) {
        put(k, Integer.toString(getInt(k)+v));
    }
    public static void decrement(String s) {
        put(s, Integer.toString(Integer.parseInt(get(s))-1));
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
