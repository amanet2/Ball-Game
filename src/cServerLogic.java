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
    static int voteskiplimit = 2;
    static int respawnwaittime = 3000;
    static int maxhp = 500;
    static int velocityplayerbase = 16;

    public static gPlayer getPlayerById(String id) {
        return scene.getPlayerById(id);
    }
}
