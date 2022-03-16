public class cServerVars extends gArgSet {
    private static gArgSet instance;

    private cServerVars() {
        super();
    }
    protected void init() {
        putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                cServerLogic.timelimit = Integer.parseInt(value);
                cServerLogic.starttime = System.currentTimeMillis();
            }
        });
        putArg(new gArg("maxhp", "500") {
            public void onChange() {
                int newval = Integer.parseInt(value);
                if(sSettings.IS_SERVER && cServerLogic.maxhp != newval) {
                    cServerLogic.maxhp = newval;
                    nServer.instance().addNetCmd("cv_maxhp " + cServerLogic.maxhp);
                    for(String s : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
                        gPlayer p = cServerLogic.scene.getPlayerById(s);
                        p.putInt("stockhp", cServerLogic.maxhp);
                    }
                }
                else if(cServerLogic.maxhp != newval){
                    cServerLogic.maxhp = newval;
                    cClientLogic.maxhp = newval;
                }
            }
        });
        putArg(new gArg("respawnwaittime", "3000") {
            public void onChange() {
                cServerLogic.respawnwaittime = Integer.parseInt(value);
            }
        });
        putArg(new gArg("velocityplayerbase", "8") {
            public void onChange() {
                int newval = Integer.parseInt(value);
                if(sSettings.IS_SERVER && cServerLogic.velocityplayerbase != newval) {
                    cServerLogic.velocityplayerbase = newval;
                    nServer.instance().addNetCmd("cv_velocityplayer " + cServerLogic.velocityplayerbase);
                }
                else if(cServerLogic.velocityplayerbase != newval){
                    cServerLogic.velocityplayerbase = newval;
                    cClientLogic.velocityPlayer = newval;
                }
            }
        });
    }
    public static gArgSet instance() {
        if(instance == null) {
            instance = new cServerVars();
            instance.init();
        }
        return instance;
    }
}
