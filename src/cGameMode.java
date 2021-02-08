import java.util.HashMap;

public class cGameMode {
    static final int DEATHMATCH = 0;
    static final int RACE = 1;
    static final int SAFE_ZONES = 2;
    static final int CAPTURE_THE_FLAG = 3;
    static final int KING_OF_FLAGS = 4;
    static final int WAYPOINTS = 5;
    static final int VIRUS = 6;
    static final int FLAG_MASTER = 7;

    static String[] net_gamemode_texts = {
            "Deathmatch",
            "Race",
            "Safe Zone",
            "Capture the Flag",
            "King of Flags",
            "Waypoints",
            "Virus",
            "Flag Master"
    };
    static String[] net_gamemode_descriptions = {
            "Kill other players for points",
            "Capture every waypoint to complete a lap",
            "Be in the zone when the timer ends",
            "Bring the red flag to the blue flag",
            "Earn points for each flag held",
            "Race to the waypoint",
            "Don't catch the virus",
            "Hold onto the flag for the longest time"
    };

    public static void resetKingOfFlags() {
        HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
        for(String id : thingMap.keySet()) {
            thingMap.get(id).put("str0", "null");
        }
    }

    public static void checkKingOfFlags() {
        if(sSettings.net_server) {
            if(cVars.getLong("kingofflagstime") < uiInterface.gameTime) {
                HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                for(String id : thingMap.keySet()) {
                    gProp flag = (gProp) thingMap.get(id);
                    gPlayer givePointPlayer = gScene.getPlayerById(flag.get("str0"));
                    if(givePointPlayer != null) {
                        xCon.ex("givepoint " + flag.get("str0"));
                    }
                }
                cVars.putLong("kingofflagstime", uiInterface.gameTime + 1000);
            }
        }
    }
}
