import java.util.ArrayList;

public class cServerLogic {
    static int timelimit = 180000;
    static long timeleft = 120000;
    static int listenPort = 5555;
    static gScene scene;
    static final gTimeEventSet timedEvents = new gTimeEventSet();

    static void changeMap(String mapPath) {
        cServerLogic.scene.clearThingMap("THING_PLAYER");
        xCon.ex("exec_new " + mapPath);
        nServer.instance().addExcludingNetCmd("server", "cl_clearthingmap THING_PLAYER");
        nServer.instance().addExcludingNetCmd("server", "cl_load");
        nServer.instance().sendMapToClients();
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
                    for(String s : new String[]{
                            String.format("echo %s#%s wins!#%s", wname, wcolor, wcolor),
                            String.format("cl_spawnpopup %s WINNER!#%s", winid, wcolor)}) {
                        nServer.instance().addExcludingNetCmd("server", s);
                    }
                }
                xCon.ex("exec_new scripts/sv_endgame");
            }
        });
        xCon.ex("setvar sv_gamemode " + cClientLogic.gamemode);
        xCon.ex("exec_new scripts/sv_startgame");
    }

    public static gPlayer getPlayerById(String id) {
        return scene.getPlayerById(id);
    }
}
