import javafx.scene.media.AudioClip;

public class cServerVars extends gArgs {
    protected static void init() {
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
                if(sSettings.framerate != sres[2])
                    sSettings.framerate = sres[2];
            }
        });
        putArg(new gArg("audioenabled", "1") {
            public void onChange() {
                sSettings.audioenabled = Integer.parseInt(value) > 0;
                if(Integer.parseInt(value) < 1) {
                    for(AudioClip c : oAudio.instance().clips) {
                        c.stop();
                    }
                    oAudio.instance().clips.clear();
                }
            }
        });
        putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                cServerLogic.starttime = System.currentTimeMillis();
            }
        });
        putArg(new gArg("displaymode", "0") {
            public void onChange() {
                sSettings.displaymode = Integer.parseInt(value);
            }
        });
        putArg(new gArg("maxhp", "500") {
            public void onChange() {
                cServerLogic.maxhp = Integer.parseInt(value);
            }
        });
    }
    public static gArgs instance() {
        if(instance == null) {
            instance = new gArgs();
            init();
        }
        return instance;
    }
}
