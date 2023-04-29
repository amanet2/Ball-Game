public class cServerVars extends gArgSet {
    static int voteskiplimit = 2;
    static int voteskipdelay = 10000;
    private static gArgSet instance;

    private cServerVars() {
        super();
    }
    
    protected void init(String[] launchArgs) {
        putArg(new gArg("listenport", "5555") {
            public void onChange() {
                cServerLogic.listenPort = Integer.parseInt(value);
            }
        });
        putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                cServerLogic.timelimit = Integer.parseInt(value);
            }
        });
        putArg(new gArg("sv_gamemode", "0") {
            public void onChange() {
                cServerLogic.gameMode = Integer.parseInt(value);
            }
        });
        putArg(new gArg("maxhp", "500") {
            public void onChange() {
                xCon.ex("cl_setvar cv_maxhp " + value);
                if(sSettings.IS_SERVER) {
                    int newmaxhp = Integer.parseInt(value);
                    nServer.instance().addNetCmd("cl_setvar cv_maxhp " + newmaxhp);
                    for (String s : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
                        gPlayer p = cServerLogic.scene.getPlayerById(s);
                        p.putInt("stockhp", newmaxhp);
                    }
                }
            }
        });
        putArg(new gArg("velocityplayerbase", "16") {
            public void onChange() {
                xCon.ex("cl_setvar cv_velocityplayer " + value);
                if(sSettings.IS_SERVER)
                    xCon.ex("addcom cl_setvar cv_velocityplayer " + Integer.parseInt(value));
            }
        });
        putArg(new gArg("voteskiplimit", "2") {
            public void onChange() {
                voteskiplimit = Integer.parseInt(value);
            }
        });
        xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_SERVER);
        loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        loadFromLaunchArgs(launchArgs);
    }
    public static gArgSet instance() {
        if(instance == null) {
            instance = new cServerVars();
        }
        return instance;
    }
}
