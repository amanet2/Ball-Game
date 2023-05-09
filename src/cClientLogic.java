import javafx.scene.media.AudioClip;

import java.awt.*;
import java.util.Collection;

public class cClientLogic {
    static int maxhp = 500;
    static double volume = 100.0;
    static String selecteditemid = "";
    static String selectedPrefabId = "";
    static String playerName = "player";
    static String playerColor = "blue";
    static int velocityPlayerBase = 16;
    static boolean debug = false;
    static boolean debuglog = false;
    static String newprefabname = "room";
    static int gamemode = 0;
    static String gamemodeTitle = "Rock Master";
    static String gamemodeText = "Rock Other Players";
    static boolean maploaded = false;
    static int prevX = 0;
    static int prevY = 0;
    static int prevW = 300;
    static int prevH = 300;
    static gScene scene;
    static gTimeEventSet timedEvents = new gTimeEventSet();
    static long serverSendTime = 0;
    static long serverRcvTime = 0;
    static int ping = 0;
    static long timeleft = 120000;
    static eGameLogicClient netClientThread;
    static gArgSet vars;

    public static gPlayer getUserPlayer() {
        return scene.getPlayerById(uiInterface.uuid);
    }

    public static Collection<String> getPlayerIds() {
        return scene.getThingMap("THING_PLAYER").keySet();
    }

    public static gPlayer getPlayerById(String id) {
        if(!scene.getThingMap("THING_PLAYER").containsKey(id))
            return null;
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }

    public static int getNewItemId() {
        int itemId = 0;
        for(String id : scene.getThingMap("THING_ITEM").keySet()) {
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        return itemId+1; //want to be the _next_ id
    }

    protected static void init(String[] launchArgs) {
        vars = new gArgSet();
        vars.putArg(new gArg("vidmode", "1920,1080,60") {
            public void onChange() {
                String[] vidmodetoks = value.split(",");
                int[] sres = new int[]{
                        Integer.parseInt(vidmodetoks[0]),
                        Integer.parseInt(vidmodetoks[1]),
                        Integer.parseInt(vidmodetoks[2])
                };
                sSettings.framerate = sres[2];
                if(sSettings.width != sres[0] || sSettings.height != sres[1]) {
                    sSettings.width = sres[0];
                    sSettings.height = sres[1];
                    //refresh fonts
                    dFonts.fontNormal = new Font(vars.get("fontui"), Font.PLAIN,
                            dFonts.fontsize * sSettings.height / sSettings.gamescale);
                    dFonts.fontGNormal = new Font(vars.get("fontui"), Font.PLAIN, dFonts.fontsize);
                    dFonts.fontSmall = new Font(vars.get("fontui"), Font.PLAIN,
                            dFonts.fontsize*sSettings.height/sSettings.gamescale/2);
                    dFonts.fontConsole = new Font(dFonts.fontnameconsole, Font.PLAIN,
                            dFonts.fontsize*sSettings.height/sSettings.gamescale/2);
                    if(oDisplay.instance().frame != null) {
                        oDisplay.instance().refreshResolution();
                        dMenus.refreshLogos();
                    }
                }
            }
        });
        vars.putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                //TODO: clean this up by holding audio clips in the proper place
                sSettings.audioenabled = Integer.parseInt(value) > 0;
                if(xMain.shellLogic != null && !sSettings.audioenabled) {
                    for(AudioClip c : xMain.shellLogic.audioClips) {
                        c.stop();
                    }
//                    oAudio.instance().clips.clear(); //maybe needed?
                }
            }
        });
        vars.putArg(new gArg("debug", "0") {
            public void onChange() {
                debug = Integer.parseInt(value) > 0;
            }
        });
        vars.putArg(new gArg("showmapmakerui", "0") {
            public void onChange() {
                sSettings.show_mapmaker_ui = Integer.parseInt(value) > 0;
            }
        });
        vars.putArg(new gArg("debuglog", "0") {
            public void onChange() {
                debuglog = Integer.parseInt(value) > 0;
            }
        });
        vars.putArg(new gArg("volume", "100") {
            public void onChange() {
                volume = Double.parseDouble(value);
            }
        });
        vars.putArg(new gArg("playercolor", "blue") {
            public void onChange() {
                playerColor = value;
            }
        });
        vars.putArg(new gArg("playername", "player") {
            public void onChange() {
                playerName = value;
            }
        });
        vars.putArg(new gArg("displaymode", "0") {
            public void onChange() {
                sSettings.displaymode = Integer.parseInt(value);
                if(oDisplay.instance().frame != null) {
                    oDisplay.instance().refreshDisplaymode();
                }
            }
        });
        vars.putArg(new gArg("vfxenableanimations", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableanimations = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        vars.putArg(new gArg("vfxenableflares", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableflares = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        vars.putArg(new gArg("vfxenableshading", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableshading = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        vars.putArg(new gArg("vfxenableshadows", "1"){
            public void onChange() {
                sSettings.vfxenableshadows = Integer.parseInt(value) > 0;
            }
        });
        vars.putArg(new gArg("gamemode", "0") {
            public void onChange() {
                gamemode = Integer.parseInt(value);
                gamemodeTitle = xCon.ex("cl_setvar GAMETYPE_"+value+"_title");
                gamemodeText = xCon.ex("cl_setvar GAMETYPE_"+value+"_text");
                if(sSettings.show_mapmaker_ui)
                    uiEditorMenus.refreshGametypeCheckBoxMenuItems();
            }
        });
        vars.putArg(new gArg("maploaded", "0") {
            public void onChange() {
                maploaded = Integer.parseInt(value) > 0;
            }
        });
        vars.putArg(new gArg("maxhp", "500") {
            public void onChange() {
                maxhp = Integer.parseInt(value);
            }
        });
        vars.putArg(new gArg("velocityplayerbase", Integer.toString(velocityPlayerBase)) {
            public void onChange() {
                velocityPlayerBase = Integer.parseInt(value);
            }
        });
        vars.putArg(new gArg("framerates", "24,30,60,75,98,120,144,165,240,320,360") {
            public void onChange() {
                String[] toks = value.split(",");
                sSettings.framerates = new int[toks.length];
                for(int i = 0; i < toks.length; i++) {
                    int tok = Integer.parseInt(toks[i].strip());
                    sSettings.framerates[i] = tok;
                }
            }
        });
        vars.putArg(new gArg("resolutions",
                "640x480,800x600,1024x768,1280x720,1280x1024,1680x1050,1600x1200,1920x1080,2560x1440,3840x2160") {
            public void onChange() {
                String[] toks = value.split(",");
                sSettings.resolutions = new String[toks.length];
                for(int i = 0; i < toks.length; i++) {
                    String tok = toks[i].strip();
                    sSettings.resolutions[i] = tok;
                }
            }
        });
        vars.putArg(new gArg("fontui", "None"));
        vars.putArg(new gArg("showfps", "0"){
            public void onChange() {
                dScreenMessages.showfps = value.equals("1");
            }
        });
        vars.putArg(new gArg("showcam", "0"){
            public void onChange() {
                dScreenMessages.showcam = value.equals("1");
            }
        });
        vars.putArg(new gArg("showmouse", "0"){
            public void onChange() {
                dScreenMessages.showmouse = value.equals("1");
            }
        });
        vars.putArg(new gArg("shownet", "0"){
            public void onChange() {
                dScreenMessages.shownet = value.equals("1");
            }
        });
        vars.putArg(new gArg("showplayer", "0"){
            public void onChange() {
                dScreenMessages.showplayer = value.equals("1");
            }
        });
        vars.putArg(new gArg("showtick", "0"){
            public void onChange() {
                dScreenMessages.showtick = value.equals("1");
            }
        });
        vars.putArg(new gArg("showscale", "0"){
            public void onChange() {
                dScreenMessages.showscale = value.equals("1");
            }
        });
        vars.putArg(new gArg("showscore", "0"){
            public void onChange() {
                dScreenMessages.showscore = value.equals("1");
            }
        });
        vars.putArg(new gArg("joinip", "localhost"){
            public void onChange() {
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
            }
        });
        vars.putArg(new gArg("joinport", "5555"){
            public void onChange() {
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
            }
        });
        vars.loadFromFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        vars.loadFromLaunchArgs(launchArgs);
    }
}
