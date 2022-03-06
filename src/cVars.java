import java.util.HashMap;

public class cVars {
    //vars for client
    private static HashMap<String, String> vars = null;

    private static void init() {
        put("gameover", "0");
        putInt("gamemode", cGameLogic.DEATHMATCH);
        put("intermissiontime", "-1");
        put("maploaded", "0");
        put("newitemname", "");
        put("newprefabname", "room");
        put("maxstockhp", "500");
        put("itemid", "0");
        put("prefabid", "0");
        put("selecteditemid", "");
        put("selectedprefabid", "");
        put("selecteditemname", "");
        put("selectedprefabname", "");
        put("shotgunmasterids", "");
        put("rechargehp", "1");
        put("respawnwaittime", "3000");
        put("serversendmapbatchsize", "320");
        put("showscore", "0");
        put("timeleft", sVars.get("timelimit"));
        put("velocityplayerbase", "8");
        put("velocityplayer", get("velocityplayerbase"));
        put("virustime", "0");
        put("weaponbotrange0", "300");
        put("weaponbotrange1", "800");
        put("weaponbotrange2", "400");
        put("weaponbotrange3", "600");
        put("weaponbotrange4", "600");
        put("weaponbotrange5", "300");
        put("weapontime0", "0");
        put("weapontime1", "0");
        put("weapontime2", "0");
        put("weapontime3", "0");
        put("weapontime4", "0");
        put("weapontime5", "0");
        put("weaponstock0", "0");
        put("weaponstock1", "0");
        put("weaponstock2", "0");
        put("weaponstock3", "0");
        put("weaponstock4", "0");
        put("weaponstock5", "0");
        //bots
        put("botthinkdelay", "500");
        put("botviruschaserange", "600");
        //voting
        put("voteskiplimit", "2");
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
