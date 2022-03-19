import javafx.scene.media.AudioClip;

import java.awt.*;

public class cClientVars extends gArgSet {
    private static gArgSet instance;

    private cClientVars() {
        super();
    }
    protected void init() {
        putArg(new gArg("vidmode", "1920,1080,60") {
            public void onChange() {
                String[] vidmodetoks = value.split(",");
                int[] sres = new int[]{
                        Integer.parseInt(vidmodetoks[0]),
                        Integer.parseInt(vidmodetoks[1]),
                        Integer.parseInt(vidmodetoks[2])
                };
                if(sSettings.width != sres[0] || sSettings.height != sres[1] || sSettings.framerate != sres[2]) {
                    sSettings.width = sres[0];
                    sSettings.height = sres[1];
                    sSettings.framerate = sres[2];
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
        putArg(new gArg("cv_maxhp", "500") {
            public void onChange() {
                cClientLogic.maxhp = Integer.parseInt(value);
            }
        });
        putArg(new gArg("cv_velocityplayer", "8") {
            public void onChange() {
                cClientLogic.velocityPlayer = Integer.parseInt(value);
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
        putArg(new gArg("joinip", "localhost"){
            public void onChange() {
                cClientLogic.joinip = value;
            }
        });
        putArg(new gArg("joinport", "5555"){
            public void onChange() {
                cClientLogic.joinport = Integer.parseInt(value);
            }
        });
    }
    public static gArgSet instance() {
        if(instance == null) {
            instance = new cClientVars();
            instance.init();
        }
        return instance;
    }
}
