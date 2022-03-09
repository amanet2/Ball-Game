import javafx.scene.media.AudioClip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class gArgs {
    private HashMap<String, gArg> args;
    private static ArrayList<String> filelines = new ArrayList<>();
    private static gArgs instance = null;

    private gArgs() {
        args = new HashMap<>();
    }

    public static boolean contains(String key) {
        return instance().args.containsKey(key);
    }

    public static void loadFromFile(String s) {
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            xCon.instance().log("Loading Settings File Path: " + s);
            String line;
            while ((line = br.readLine()) != null) {
                filelines.add(line);
                String[] args = line.split(" ");
                String argname = args[0];
                if(argname.trim().replace(" ","").charAt(0) != '#') //filter out comments
                    put(argname, line.replaceFirst(argname + " ", ""));
            }
            xCon.instance().log(instance().args.toString());
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    private static void init() {
        putArg(new gArg("vidmode", "1920,1080,60") {
            public void onChange() {
                String[] vidmodetoks = value.split(",");
                int[] sres = new int[]{
                        Integer.parseInt(vidmodetoks[0]),
                        Integer.parseInt(vidmodetoks[1]),
                        Integer.parseInt(vidmodetoks[2])
                };
                if(sSettings.width != sres[0] || sSettings.height != sres[1]) {
                    sSettings.width = sres[0];
                    sSettings.height = sres[1];
                    oDisplay.instance().refreshResolution();
                    dMenus.refreshLogos();
                }
                if(sSettings.framerate != sres[2]) {
                    sSettings.framerate = sres[2];
//                    uiMenus.menuSelection[uiMenus.MENU_VIDEO].items[1].text =
//                            String.format("Framerate: [%d]",sSettings.framerate);
                }
            }
        });
        putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                if(Integer.parseInt(value) < 1) {
                    for(AudioClip c : oAudio.instance().clips) {
                        c.stop();
                    }
                    oAudio.instance().clips.clear();
                }
            }
        });
        putArg(new gArg("timelimit", "120000") {
            public void onChange() {
                cServerLogic.starttime = System.currentTimeMillis();
            }
        });
    }

    private static void putArg(gArg arg) {
        instance().args.put(arg.key, arg);
    }

    private static gArg getArg(String key) {
        return instance().args.get(key);
    }

    public static String get(String key) {
        if(instance().args.containsKey(key))
            return instance().args.get(key).value;
        return null;
    }

    public static int getInt(String key) {
        if(instance().args.containsKey(key))
            return Integer.parseInt(instance().args.get(key).value);
        return -1;
    }

    public static void put(String key, String val) {
        gArg arg = getArg(key);
        if(arg != null) {
            arg.value = val;
            arg.onChange();
        }
        else
            xCon.instance().debug("gArgs.put: no arg for key: " + key);
    }

    private static gArgs instance() {
        if(instance == null) {
            instance = new gArgs();
            init();
        }
        return instance;
    }
}
