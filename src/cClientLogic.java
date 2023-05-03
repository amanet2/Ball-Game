import java.util.Collection;

public class cClientLogic {
    static int maxhp = 500;
    static double volume = 100.0;
    static String selecteditemid = "";
    static String selectedPrefabId = "";
    static String playerName = "player";
    static String playerColor = "blue";
    static int velocityPlayer = 64;
    static boolean debug = false;
    static boolean debuglog = false;
    static String newprefabname = "room";
    static int gamemode = 0;
    static String gamemodeTitle = "Rock Master";
    static String gamemodeText = "Rock Other Players";
    static boolean maploaded = false;
    static int prevX = 0;
    static int prevY = 0;
    static int prevW = 300;
    static int prevH = 300;
    static gScene scene;
    static gTimeEventSet timedEvents = new gTimeEventSet();
    static long serverSendTime = 0;
    static long serverRcvTime = 0;
    static int ping = 0;
    static long timeleft = 120000;
    static nClient netClientThread;

    public static gPlayer getUserPlayer() {
        return scene.getPlayerById(uiInterface.uuid);
    }

    public static Collection<String> getPlayerIds() {
        return scene.getThingMap("THING_PLAYER").keySet();
    }

    public static gPlayer getPlayerById(String id) {
        if(!scene.getThingMap("THING_PLAYER").containsKey(id))
            return null;
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }

    public static int getNewItemId() {
        int itemId = 0;
        for(String id : scene.getThingMap("THING_ITEM").keySet()) {
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        return itemId+1; //want to be the _next_ id
    }
}
