import java.util.Arrays;
import java.util.HashMap;

public class cVars {
    //vars for client
    private static HashMap<String, String> vars = null;

    private static void init() {
        put("bottime", "0");
        putInt("cammode", gCamera.MODE_FREE);
        put("cammov0", "0");
        put("cammov1", "0");
        put("cammov2", "0");
        put("cammov3", "0");
        put("camplayertrackingid", uiInterface.uuid);
        put("camplayertrackingindex", "0");
        put("camx", "0");
        put("camy", "0");
        put("delayhp", "2400");
        put("delaypow", "500");
        put("delayweap", "1800");
        put("flagmastertime", "0");
        put("flashlight", "0");
        put("gamescale", "2160");
        put("gametick", "240");
        put("gamewon", "0");
        putInt("gamemode", cGameLogic.DEATHMATCH);
        put("gamemodelist", Arrays.toString(cGameLogic.net_gamemode_texts));
        put("blockmouseui", "0");
        put("intermissiontime", "-1");
        put("maploaded", "0");
        put("mapname", "new");
        put("newitemname", "");
        put("newprefabname", "room_small");
        put("maxstockhp", "500");
        put("popuplivetime", "2000");
        put("itemid", "0");
        put("prefabid", "0");
        put("selecteditemid", "");
        put("selectedprefabid", "");
        put("selecteditemname", "");
        put("selectedprefabname", "");
        put("shotgunmasterids", "");
        put("rechargepow", "12");
        put("rechargehp", "1");
        put("respawnwaittime", "3000");
        put("scoremap", "");
        put("serversendmapbatchsize", "4");
        put("showscore", "0");
        put("starttime", "0");
        put("timeleft", sVars.get("timelimit"));
        put("velocitycam", "9");
        put("velocityplayerbase", "8");
        put("velocityplayer", get("velocityplayerbase"));
        put("velocitypopup", "2");
        put("vfxuialphahp", "230");
        put("vfxshadowalpha1", "200");
        put("vfxshadowalpha2", "0");
        put("vfxcornershadowalpha1", "200");
        put("vfxcornershadowalpha2", "0");
        put("vfxshadowfactor", "0.3");
        put("vfxflooroutlinealpha1", "0");
        put("vfxflooroutlinealpha2", "0");
        put("vfxfloorshadingalpha1", "0");
        put("vfxfloorshadingalpha2", "0");
        put("vfxroofvertoutlinealpha1", "20");
        put("vfxroofvertoutlinealpha2", "90");
        put("vfxroofvertshadingalpha1", "0");
        put("vfxroofvertshadingalpha2", "20");
        put("vfxroofoutlinealpha1", "100");
        put("vfxroofoutlinealpha2", "0");
        put("vfxroofshadingalpha1", "100");
        put("vfxroofshadingalpha2", "0");
        put("vfxwalloutlinealpha1", "80");
        put("vfxwalloutlinealpha2", "255");
        put("vfxwallshadingalpha1", "80");
        put("vfxwallshadingalpha2", "220");
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
        put("botbehavior", "NONE");
        put("botthinkdelay", "1000");
        put("botviruschaserange", "600");
        //voting
        put("voteskipctr", "0");
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
