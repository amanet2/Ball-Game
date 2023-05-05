import java.util.ArrayList;

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
    static int voteskipdelay = 10000;
    static int respawnwaittime = 3000;
    static int maxhp = 500;
    static int velocityplayerbase = 16;
    static ArrayList<String> voteSkipList = new ArrayList<>();

    static void changeMap(String mapPath) {
        xCon.ex("loadingscreen");
        xCon.ex("exec " + mapPath); //by exec'ing the map, server is actively streaming blocks
        xCon.ex("-loadingscreen");
        if(!sSettings.show_mapmaker_ui) { //spawn in after finished loading
            for(String id : netServerThread.masterStateMap.keys()) {
                netServerThread.addNetCmd("server", "respawnnetplayer " + id);
            }
        }
        //reset game state
        gScoreboard.resetScoresMap();
        voteSkipList = new ArrayList<>();
        timedEvents.clear();
        if(sSettings.show_mapmaker_ui)
            return;
        long starttime = gTime.gameTime;
        for (long t = starttime + 1000; t <= starttime + timelimit; t += 1000) {
            long lastT = t;
            timedEvents.put(Long.toString(t), new gTimeEvent() {
                public void doCommand() {
                    if (timelimit > 0)
                        timeleft = Math.max(0, (starttime + timelimit) - lastT);
                }
            });
        }
        timedEvents.put(Long.toString(starttime + timelimit), new gTimeEvent() {
            public void doCommand() {
                //select winner and run postgame script
                String winid = gScoreboard.getWinnerId();
                if(!winid.equalsIgnoreCase("null")) {
                    nState winState = netServerThread.masterStateMap.get(winid);
                    String wname = winState.get("name");
                    String wcolor = winState.get("color");
                    xCon.ex("givewin " + winid);
                    xCon.ex(String.format("echo %s#%s wins!#%s", wname, wcolor, wcolor));
                    xCon.ex(String.format("spawnpopup %s WINNER!#%s", winid, wcolor));
                }
                xCon.ex("exec scripts/sv_endgame");
            }
        });
        xCon.ex("exec scripts/sv_startgame");
    }

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
                    for (String s : scene.getThingMap("THING_PLAYER").keySet()) {
                        gPlayer p = scene.getPlayerById(s);
                        p.putInt("stockhp", newmaxhp);
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
