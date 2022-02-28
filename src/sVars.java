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
        keys.put("botnameselection", "bot0,bot1,bot2,bot3,bot4,bot5,bot6,bot7,bot8,bot9");
        keys.put("colorselection", "blue,green,orange,pink,purple,red,teal,yellow");
        keys.put("consolemaxlinelength", "128");
        keys.put("coverpath", "misc/cover.png");
        keys.put("datapath", "ballgame");
        keys.put("debug", "0");
        keys.put("debuglog", "0");
        keys.put("displaymode", "0");
        keys.put("fontmode", "0");
        keys.put("fontsize", "90");
        keys.put("fontnameui", "None");
        keys.put("fontnameconsole", "Courier");
        keys.put("framerates", "24,30,60,75,98,120,144,240");
        keys.put("inconsole", "0");
        keys.put("intermissiontime", "10000");
        keys.put("joinip", "localhost");
        keys.put("joinport", "5555");
        keys.put("logopath", "misc/logo.png");
        keys.put("msgfadetime", "10000");
        keys.put("netrcvretries", "0");
        keys.put("playercolor", "blue");
        keys.put("playerhat", "none");
        keys.put("playername", "player");
        keys.put("ratebots", "30");
        keys.put("rateclient", "60");
        keys.put("rateserver", "500");
        keys.put("rcvbytesclient", "2048");
        keys.put("rcvbytesserver", "512");
        keys.put("resolutions", "640x480,800x600,1024x768,1280x720,1280x1024,1600x1200,1920x1080,2560x1440,3840x2160");
        keys.put("sfxrange", "1800");
        keys.put("showcam", "1");
        keys.put("showfps", "1");
        keys.put("showmapmakerui", "0");
        keys.put("showmouse", "1");
        keys.put("shownet", "1");
        keys.put("showplayer", "1");
        keys.put("showscale", "1");
        keys.put("showtick", "1");
        keys.put("smoothing", "1");
        keys.put("startpaused", "1"); //ISSUE: needs to be 1 HERE for mapmaker to be a good exp
        keys.put("timelimit", "120000");
        keys.put("timeout", "10000");
        keys.put("vidmode", "1280,720,60");
        keys.put("volume", "100");
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
                || k.equals("rateclient") || k.equals("rateserver")
                || k.equals("rcvbytesclient")
                || k.equals("rcvbytesserver") || k.equals("sfxrange") || k.equals("showfps") || k.equals("shownet")
                || k.equals("smoothing") || k.equals("startpaused")
                || k.equals("timelimit") || k.equals("timeout")
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
        refresh();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            xCon.instance().log("Loading Settings File Path: " + s);
            String line;
            while ((line = br.readLine()) != null) {
                filelines.add(line);
                String[] args = line.split(" ");
                String argname = args[0];
                if(argname.trim().replace(" ","").charAt(0) != '#') //filter out comments
                    keys.put(argname, line.replaceFirst(argname+" ", ""));
            }
            xCon.instance().debug(keys.toString());
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static void readLaunchArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args.length >= i+1) {
                sVars.put(args[i], args[i+1]);
                i+=1;
            }
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
