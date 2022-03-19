import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class sVars {
    private static ArrayList<String> filelines = new ArrayList<>();
    private static HashMap<String, String> keys = null;

    private static void init() {
        keys.put("botnameselection", "bot0,bot1,bot2,bot3,bot4,bot5,bot6,bot7,bot8,bot9");
        keys.put("colorselection", "blue,green,orange,pink,purple,red,teal,yellow");
        keys.put("datapath", "ballgame");
        keys.put("debuglog", "0");
        keys.put("framerates", "24,30,60,75,98,120,144,240");
        keys.put("joinport", "5555");
        keys.put("resolutions", "640x480,800x600,1024x768,1280x720,1280x1024,1600x1200,1920x1080,2560x1440,3840x2160");
        keys.put("showmapmakerui", "0");
    }

    static boolean checkVal(String key, String v) {
        try {
            String k = key.toLowerCase();
            if(k.contains("time") || k.equals("framerates") || k.equals("joinport")) {
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
