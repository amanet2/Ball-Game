import java.util.ArrayList;

public class cServerLogic {
    static int timelimit = 180000;
    static long timeleft = 120000;
    static int listenPort = 5555;
    static gScene scene;
    static final gTimeEventSet timedEvents = new gTimeEventSet();

    static void changeMap(String mapPath) {
        cServerLogic.scene.clearThingMap("THING_PLAYER");
//        xCon.ex("exec scripts/sv_resetgamestate");
        xCon.ex("exec " + mapPath);
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
                xCon.ex("exec scripts/sv_endgame");
            }
        });
        xCon.ex("exec scripts/sv_startgame " + cClientLogic.gamemode);
    }

    public static gPlayer getPlayerById(String id) {
        return scene.getPlayerById(id);
    }
}
