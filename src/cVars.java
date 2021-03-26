import java.util.Arrays;
import java.util.HashMap;

public class cVars {
    //vars for client
    private static HashMap<String, String> vars = null;

    private static void init() {
        put("ballx", "0");
        put("bally", "0");
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
        put("clipplayer", "1");
        put("collideplayers", "1");
        put("delayhp", "2400");
        put("delaypow", "500");
        put("delayweap", "1800");
        put("disconnectconfirmed", "0");
        put("disconnecting", "0");
        put("exitteleportertag", "-1");
        put("fallkilltime", "800");
        put("falltime", "0");
        put("flagmasterid", "");
        put("flagmastertime", "0");
        put("flashlight", "0");
        put("gamescale", "2160");
        put("gametick", "240");
        put("gamewon", "0");
        putInt("gamemode", cGameMode.DEATHMATCH);
        put("gamemodelist", Arrays.toString(cGameMode.net_gamemode_texts));
        put("gameteam", "0");
        put("gravity", "0");
        put("blockmouseui", "0");
        put("inspawn", "1");
        put("intermissiontime", "-1");
        put("jumptime", "0");
        put("jumpheight", "0");
        put("jumping", "0");
        put("jumpsquish", "20");
        put("jumptimemax", "100");
        put("delayjump", "600");
        put("kingofflagstime", "0");
        put("knocksoundtime", "0");
        put("knocksoundtimegap", "150");
        put("maploaded", "0");
        put("maxstockhp", "500");
        put("maxstockspeed", "200");
        put("popuplivetime", "2000");
        put("powerupson", sVars.get("powerupsmaxon"));
        put("powerupstime", "0");
        put("quitconfirmed", "0");
        put("quitting", "0");
        put("rechargepow", "12");
        put("rechargehp", "1");
        put("respawnwaittime", "3000");
        put("safezonetime", "-1");
        put("scoremap", "");
        put("sendpowerup", "");
        put("serversendmapbatchsize", "4");
        put("serveraddbotstime", "0");
        put("showscore", "0");
        put("sicknessslow", "0");
        put("sicknessfast", "0");
        put("spawnprotectionmaxtime", "1000");
        put("sprint", "0");
        put("sprinttime", "0");
        put("starttime", "0");
        put("stockspeed", get("maxstockspeed"));
        put("suppressknocksound", "0");
        put("timeleft", sVars.get("timelimit"));
        put("velocitycam", "9");
        put("velocityplayerbase", "8");
        put("velocityplayer", get("velocityplayerbase"));
        put("velocitypopup", "2");
        put("velocitysuperspeed", "16");
        put("vfxuialphaflashlight", "128");
        put("vfxuialphasprint", "128");
        put("vfxuialphahp", "230");
        put("vfxshadowalpha1", "200");
        put("vfxshadowalpha2", "0");
        put("vfxcornershadowalpha1", "200");
        put("vfxcornershadowalpha2", "0");
        put("vfxshadowfactor", "0.3");
        put("vfxflooroutlinealpha1", "0");
        put("vfxflooroutlinealpha2", "10");
        put("vfxfloorshadingalpha1", "85");
        put("vfxfloorshadingalpha2", "80");
        put("vfxroofvertoutlinealpha1", "20");
        put("vfxroofvertoutlinealpha2", "90");
        put("vfxroofvertshadingalpha1", "0");
        put("vfxroofvertshadingalpha2", "20");
        put("vfxroofoutlinealpha1", "30");
        put("vfxroofoutlinealpha2", "0");
        put("vfxroofshadingalpha1", "80");
        put("vfxroofshadingalpha2", "0");
        put("vfxwalloutlinealpha1", "60");
        put("vfxwalloutlinealpha2", "220");
        put("vfxwallshadingalpha1", "90");
        put("vfxwallshadingalpha2", "200");
        put("virusids", "");
        put("virusresetwaittime", "5000");
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
        put("winnerid", "");
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
    public static String[] getArray(String s) {
        return get(s).split(",");
    }
    public static int[] getIntArray(String s) {
        String[] sarr = getArray(s);
        int[] iarr = new int[sarr.length];
        for(int i = 0; i < sarr.length; i++) {
            iarr[i] = Integer.parseInt(sarr[i]);
        }
        return iarr;
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
    public static void putArray(String k, String[] v) {
        String trimstr = Arrays.toString(v).replace(" ","");
        put(k, trimstr.substring(1,trimstr.length()-1));
    }
    public static void putInArray(String k, String v, int i) {
        String[] sarr = getArray(k);
        sarr[i] = v;
        String trimstr = Arrays.toString(sarr).replace(" ","");
        put(k, trimstr.substring(1,trimstr.length()-1));
    }
    public static void increment(String s) {
        put(s, Integer.toString(Integer.parseInt(get(s))+1));
    }
    public static void addIntVal(String k, int v) {
        put(k, Integer.toString(getInt(k)+v));
    }
    public static void decrement(String s) {
        put(s, Integer.toString(Integer.parseInt(get(s))-1));
    }
    public static void flip(String s) {
        put(s, isOne(s) ? "0" : "1");
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
    public static void reset() {
        vars = null;
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

    public static void assignRandomPlayerIdToVar(String cvar) {
        int r = (int) (Math.random()*((double)gScene.getPlayerIds().size()));
        int c = 0;
        for(String id : gScene.getPlayerIds()) {
            if(c==r)
                cVars.put(cvar, id);
            c++;
        }
    }
}
