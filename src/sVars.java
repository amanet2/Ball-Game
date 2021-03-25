import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class sVars {
    private static ArrayList<String> filelines = new ArrayList<>();
    private static HashMap<String, String> keys = null;

    private static void init() {
        keys.put("audioenabled", "1");
        keys.put("bgcolor", "0,0,0");
        keys.put("botcount", "0");
        keys.put("botcountmax", "3");
        keys.put("botnameselection", "sneed,chuck,based,janny,CIA,dunnhier,dubs,trips,quads,digits");
        keys.put("colorselection", "blue,green,orange,pink,purple,red,teal,yellow");
        keys.put("consolemaxlinelength", "128");
        keys.put("coverpath", "misc/cover.png");
        keys.put("datapath", "ballgame");
        keys.put("prefabspath", "prefabs");
        keys.put("debug", "0");
        keys.put("debuglog", "0");
        keys.put("defaultexec", "config/autoexec.cfg");
        keys.put("defaultmap", "mainmenu.map");
        keys.put("defaulttitle", "Ball Game");
        keys.put("displaymode", "0");
        keys.put("fontcoloralert", "200,0,50,200");
        keys.put("fontcolorbonus", "50,220,100,200");
        keys.put("fontcolorhighlight", "220,175,0,255");
        keys.put("fontcolornormal", "255,255,255,200");
        keys.put("fontmode", "0");
        keys.put("fontsize", "90");
        keys.put("fontnameui", "None");
        keys.put("fontnameconsole", "Courier");
        keys.put("framerates", "24,30,60,75,100,120,144,240,320,1000");
        keys.put("hatselection", "none,winter,cowboy,raincover,stovepipe_normal,stovepipe_irish,bow,witch,skull,boomer");
        keys.put("inconsole", "0");
        keys.put("intermissiontime", "10000");
        keys.put("joinip", "localhost");
        keys.put("joinport", "5555");
        keys.put("logopath", "misc/logo.png");
        keys.put("mapextension", ".map");
        keys.put("msgfadetime", "6000");
        keys.put("netrcvretries", "0");
        keys.put("ratebots", "30");
        keys.put("rateclient", "30");
        keys.put("rateserver", "500");
        keys.put("playerarrow", "0");
        keys.put("playercolor", "blue");
        keys.put("playerhat", "none");
        keys.put("playername", "player");
        keys.put("powerupsmaxon", "8");
        keys.put("powerupsusetimemax", "20000");
        keys.put("powerupswaittime", "10000");
        keys.put("rcvbytesclient", "2048");
        keys.put("rcvbytesserver", "512");
        keys.put("resolutions", "640x480,800x600,1024x768,1280x720,1280x1024,1600x1200,1920x1080,2560x1440,3840x2160");
        keys.put("safezonetime", "20000");
        keys.put("scorelimit", "1000");
        keys.put("sfxrange", "1800");
        keys.put("showtick", "0");
        keys.put("showfps", "0");
        keys.put("shownet", "0");
        keys.put("showscale", "1");
        keys.put("smoothing", "1");
        keys.put("startpaused", "0");
        keys.put("timelimit", "120000");
        keys.put("timeout", "1000");
        keys.put("vfxenableanimations", "1");
        keys.put("vfxenableflares", "1");
        keys.put("vfxenableshading", "1");
        keys.put("vfxenableshadows", "1");
        keys.put("vfxfactor", "144");
        keys.put("vfxfactordiv", "16");
        keys.put("vidmode", "1024,768,60");
        keys.put("volume", "50");
    }

    static boolean checkVal(String key, String v) {
        try {
            String k = key.toLowerCase();
            if(k.equals("vidmode")) {
                String[] toks = v.split(",");
                return toks.length == 3 && Integer.parseInt(toks[0]) >= 640 && Integer.parseInt(toks[1]) >= 480
                        && Integer.parseInt(toks[2]) >= 24;
            }
            else if(k.contains("bgcolor") || k.contains("fontcolor")) {
                String[] toks = v.split(",");
                return (k.contains("bgcolor") && toks.length == 3 && Integer.parseInt(toks[0]) >= 0
                        && Integer.parseInt(toks[1]) >= 0 && Integer.parseInt(toks[2]) >= 0
                        && Integer.parseInt(toks[0]) < 256 && Integer.parseInt(toks[1]) < 256
                        && Integer.parseInt(toks[2]) < 256)
                        || (k.contains("fontcolor") && toks.length == 4 && Integer.parseInt(toks[0]) >= 0
                        && Integer.parseInt(toks[1]) >= 0 && Integer.parseInt(toks[2]) >= 0
                        && Integer.parseInt(toks[3]) >= 0 && Integer.parseInt(toks[0]) < 256
                        && Integer.parseInt(toks[1]) < 256 && Integer.parseInt(toks[2]) < 256
                        && Integer.parseInt(toks[3]) < 256);
            }
            else if(k.contains("time") || k.equals("audioenabled")
                || k.equals("debug") || k.equals("displaymode") || k.equals("fontmode") || k.equals("fontsize")
                || k.equals("framerates") || k.equals("gamescale") || k.equals("gametick") || k.equals("inconsole")
                || k.equals("intermissiontime") || k.equals("joinport") || k.equals("msgfadetime")
                || k.equals("rateclient") || k.equals("rateserver") || k.equals("powerupsmaxon")
                || k.equals("powerupsusetimemax") || k.equals("powerupswaittime") || k.equals("rcvbytesclient")
                || k.equals("rcvbytesserver") || k.equals("safezonetime")
                || k.equals("scorelimit") || k.equals("sfxrange") || k.equals("showfps") || k.equals("shownet")
                || k.equals("smoothing") || k.equals("startpaused")
                || k.equals("timelimit") || k.equals("timeout")
                || k.equals("vfxfactor") || k.equals("vfxfactordiv")
                || (k.equals("volume") && Integer.parseInt(v) <= 100)
            ) {
                return Integer.parseInt(v) >= 0;
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
        return keys.get(s);
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

    public static String[] getArray(String k) {
        return get(k).split(",");
    }

    public static boolean isOne(String s) {
        return get(s).equals("1");
    }

    public static boolean isZero(String s) {
        return get(s).equals("0");
    }

    public static boolean isIntVal(String k, int v) {
        return getInt(k) == v;
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

    public static void reset() {
        keys = null;
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

    public static void loadFromFile(String s) {
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            xCon.instance().log("Loading Settings File Path: " + s);
            refresh();
            String line;
            while ((line = br.readLine()) != null) {
                String[] args = line.split(" ");
                String argname = args[0];
                filelines.add(line);
                if(keys.containsKey(argname))
                    keys.put(argname,line.replaceFirst(argname+" ", ""));
                if(line.trim().length() > 0 && line.trim().charAt(0) != '#')
                    sLaunchArgs.readLaunchArguments(argname,args);
            }
            xCon.instance().debug(keys.toString());
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static void saveFile(String s) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(s), StandardCharsets.UTF_8))) {
            for(String line : filelines) {
                String arg = line.split(" ")[0];
                if(keys.containsKey(arg))
                    writer.write(String.format("%s %s", arg, keys.get(arg)));
                else
                    writer.write(line);
                writer.write("\n");
            }
        } catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }
}
