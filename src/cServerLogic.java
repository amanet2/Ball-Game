import java.util.ArrayList;

public class cServerLogic {
    static int timelimit = 180000;
    static long timeleft = 120000;
    static int listenPort = 5555;
    static gScene scene;
    static final gTimeEventSet timedEvents = new gTimeEventSet();
    static boolean isLoadingFromHDD = false;
    static int gameMode = 0;

    static void changeMap(String mapPath) {
        xCon.ex("loadingscreen");
        xCon.ex("exec_new " + mapPath); //by exec'ing the map, server is actively streaming blocks
        xCon.ex("-loadingscreen");
        if(!sSettings.show_mapmaker_ui) { //spawn in after finished loading
            for(String id : nServer.instance().masterStateMap.keys()) {
                nServer.instance().addNetCmd("server", "respawnnetplayer " + id);
            }
        }
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipList = new ArrayList<>();
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
        timedEvents.put(Long.toString(starttime + cServerLogic.timelimit), new gTimeEvent() {
            public void doCommand() {
                //select winner and run postgame script
                String winid = gScoreboard.getWinnerId();
                if(!winid.equalsIgnoreCase("null")) {
                    nState winState = nServer.instance().masterStateMap.get(winid);
                    String wname = winState.get("name");
                    String wcolor = winState.get("color");
                    xCon.ex("givewin " + winid);
                    xCon.ex(String.format("echo %s#%s wins!#%s", wname, wcolor, wcolor));
                    xCon.ex(String.format("spawnpopup %s WINNER!#%s", winid, wcolor));
                }
                xCon.ex("exec_new scripts/sv_endgame");
            }
        });
        xCon.ex("exec_new scripts/sv_startgame");
    }

    public static gPlayer getPlayerById(String id) {
        return scene.getPlayerById(id);
    }
}
