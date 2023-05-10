public class cServerLogic {
    static int timelimit = 180000;
    static long timeleft = 120000;
    static int listenPort = 5555;
    static gScene scene;
    static final gTimeEventSet timedEvents = new gTimeEventSet();
    static boolean isLoadingFromHDD = false;
    static int gameMode = 0;
    static eGameLogicSimulation localGameThread;
    static eGameLogicServer netServerThread;
    static gArgSet vars;
    static int voteskiplimit = 2;
    static int respawnwaittime = 3000;
    static int maxhp = 500;
    static int velocityplayerbase = 16;

    public static gPlayer getPlayerById(String id) {
        return scene.getPlayerById(id);
    }

    public static void init(String[] launchArgs) {
        vars = new gArgSet();

        vars.putArg(new gArg("listenport", "5555") {
            public void onChange() {
                listenPort = Integer.parseInt(value);
            }
        });
        vars.putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                timelimit = Integer.parseInt(value);
            }
        });
        vars.putArg(new gArg("gamemode", "0") {
            public void onChange() {
                gameMode = Integer.parseInt(value);
            }
        });
        vars.putArg(new gArg("maxhp", Integer.toString(maxhp)) {
            public void onChange() {
                maxhp = Integer.parseInt(value);
                if(sSettings.IS_SERVER) {
                    int newmaxhp = Integer.parseInt(value);
                    netServerThread.addIgnoringNetCmd("server", "cl_setvar maxhp " + newmaxhp);
                    nStateMap cState = new nStateMap(cServerLogic.netServerThread.masterStateSnapshot);
                    for(String clid : cState.keys()) {
                        netServerThread.setClientState(clid, "hp", value);
                    }
                }
            }
        });
        vars.putArg(new gArg("velocityplayerbase", Integer.toString(velocityplayerbase)) {
            public void onChange() {
                velocityplayerbase = Integer.parseInt(value);
                if(sSettings.IS_SERVER)
                    xCon.ex("addcom cl_setvar velocityplayerbase " + velocityplayerbase);
            }
        });
        vars.putArg(new gArg("voteskiplimit", "2") {
            public void onChange() {
                voteskiplimit = Integer.parseInt(value);
                if(netServerThread != null)
                    netServerThread.checkForVoteSkip();
            }
        });
        vars.putArg(new gArg("respawnwaittime", Integer.toString(respawnwaittime)) {
            public void onChange() {
                respawnwaittime = Integer.parseInt(value);
            }
        });
        vars.loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        vars.loadFromLaunchArgs(launchArgs);
    }
}
