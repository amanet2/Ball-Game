import javafx.scene.media.AudioClip;
import java.awt.Font;

public class cClientVars extends gArgSet {
    private static gArgSet instance;

    private cClientVars() {
        super();
    }
    protected void init(String[] launchArgs) {
        putArg(new gArg("vidmode", "1920,1080,60") {
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
                    dFonts.fontNormal = new Font(cClientVars.instance().get("fontui"), Font.PLAIN,
                            dFonts.fontsize * sSettings.height / sSettings.gamescale);
                    dFonts.fontGNormal = new Font(cClientVars.instance().get("fontui"), Font.PLAIN, dFonts.fontsize);
                    dFonts.fontSmall = new Font(cClientVars.instance().get("fontui"), Font.PLAIN,
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
        putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                sSettings.audioenabled = Integer.parseInt(value) > 0;
                if(!sSettings.audioenabled) {
                    for(AudioClip c : oAudio.instance().clips) {
                        c.stop();
                    }
                    oAudio.instance().clips.clear();
                }
            }
        });
        putArg(new gArg("debug", "0") {
            public void onChange() {
                cClientLogic.debug = Integer.parseInt(value) > 0;
            }
        });
        putArg(new gArg("showmapmakerui", "0") {
            public void onChange() {
                sSettings.show_mapmaker_ui = Integer.parseInt(value) > 0;
            }
        });
        putArg(new gArg("debuglog", "0") {
            public void onChange() {
                cClientLogic.debuglog = Integer.parseInt(value) > 0;
            }
        });
        putArg(new gArg("volume", "100") {
            public void onChange() {
                cClientLogic.volume = Double.parseDouble(value);
            }
        });
        putArg(new gArg("playercolor", "blue") {
            public void onChange() {
                cClientLogic.playerColor = value;
            }
        });
        putArg(new gArg("playername", "player") {
            public void onChange() {
                cClientLogic.playerName = value;
            }
        });
        putArg(new gArg("displaymode", "0") {
            public void onChange() {
                sSettings.displaymode = Integer.parseInt(value);
                if(oDisplay.instance().frame != null) {
                    oDisplay.instance().refreshDisplaymode();
                }
            }
        });
        putArg(new gArg("vfxenableanimations", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableanimations = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        putArg(new gArg("vfxenableflares", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableflares = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        putArg(new gArg("vfxenableshading", "1"){
            public void onChange() {
                try {
                    sSettings.vfxenableshading = Integer.parseInt(value) == 1;
                }
                catch (Exception ignored) {

                }
            }
        });
        putArg(new gArg("vfxenableshadows", "1"){
            public void onChange() {
                sSettings.vfxenableshadows = Integer.parseInt(value) > 0;
            }
        });
        putArg(new gArg("cv_gamemode", "0") {
            public void onChange() {
                cClientLogic.gamemode = Integer.parseInt(value);
                cClientLogic.gamemodeTitle = xCon.ex("setvar GAMETYPE_"+value+"_title");
                cClientLogic.gamemodeText = xCon.ex("setvar GAMETYPE_"+value+"_text");
                if(sSettings.show_mapmaker_ui)
                    uiEditorMenus.refreshGametypeCheckBoxMenuItems();
            }
        });
        putArg(new gArg("cv_maploaded", "0") {
            public void onChange() {
                cClientLogic.maploaded = Integer.parseInt(value) > 0;
            }
        });
        putArg(new gArg("cv_maxhp", "500") {
            public void onChange() {
                cClientLogic.maxhp = Integer.parseInt(value);
            }
        });
        putArg(new gArg("cv_velocityplayer", "64") {
            public void onChange() {
                cClientLogic.velocityPlayer = Integer.parseInt(value);
            }
        });
        putArg(new gArg("framerates", "24,30,60,75,98,120,144,165,240,320,360") {
            public void onChange() {
                String[] toks = value.split(",");
                sSettings.framerates = new int[toks.length];
                for(int i = 0; i < toks.length; i++) {
                    int tok = Integer.parseInt(toks[i].strip());
                    sSettings.framerates[i] = tok;
                }
            }
        });
        putArg(new gArg("resolutions",
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
        putArg(new gArg("fontui", "None"));
        putArg(new gArg("showfps", "0"){
            public void onChange() {
                dScreenMessages.showfps = value.equals("1");
            }
        });
        putArg(new gArg("showcam", "0"){
            public void onChange() {
                dScreenMessages.showcam = value.equals("1");
            }
        });
        putArg(new gArg("showmouse", "0"){
            public void onChange() {
                dScreenMessages.showmouse = value.equals("1");
            }
        });
        putArg(new gArg("shownet", "0"){
            public void onChange() {
                dScreenMessages.shownet = value.equals("1");
            }
        });
        putArg(new gArg("showplayer", "0"){
            public void onChange() {
                dScreenMessages.showplayer = value.equals("1");
            }
        });
        putArg(new gArg("showtick", "0"){
            public void onChange() {
                dScreenMessages.showtick = value.equals("1");
            }
        });
        putArg(new gArg("showscale", "0"){
            public void onChange() {
                dScreenMessages.showscale = value.equals("1");
            }
        });
        putArg(new gArg("showscore", "0"){
            public void onChange() {
                dScreenMessages.showscore = value.equals("1");
            }
        });
        putArg(new gArg("joinip", "localhost"){
            public void onChange() {
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
            }
        });
        putArg(new gArg("joinport", "5555"){
            public void onChange() {
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
            }
        });
        loadFromFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        loadFromLaunchArgs(launchArgs);
    }
    public static gArgSet instance() {
        if(instance == null)
            instance = new cClientVars();
        return instance;
    }
}
