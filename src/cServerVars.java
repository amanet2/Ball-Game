public class cServerVars extends gArgSet {
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
        putArg(new gArg("maxhp", "500") {
            public void onChange() {
                int newmaxhp = Integer.parseInt(value);
                nServer.instance().addNetCmd("cv_maxhp " + newmaxhp);
                for(String s : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
                    gPlayer p = cServerLogic.scene.getPlayerById(s);
                    p.putInt("stockhp", newmaxhp);
                }
            }
        });
        putArg(new gArg("velocityplayerbase", "8") {
            public void onChange() {
                if(sSettings.IS_SERVER)
                    xCon.ex("addcom cv_velocityplayer " + Integer.parseInt(value));
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
