import java.util.HashMap;

public class cGameMode {
    static final int DEATHMATCH = 0;
    static final int RACE = 1;
    static final int SAFE_ZONES = 2;
    static final int CAPTURE_THE_FLAG = 3;
    static final int KING_OF_FLAGS = 4;
    static final int WAYPOINTS = 5;
    static final int VIRUS = 6;
    static final int VIRUS_SINGLE = 7;
    static final int FLAG_MASTER = 8;
    static final int BOUNCYBALL = 9;
    static final int CHOSENONE = 10;
    static final int ANTI_CHOSENONE = 11;

    static String[] net_gamemode_texts = {
            "Deathmatch",
            "Race",
            "Safe Zone",
            "Capture the Flag",
            "King of Flags",
            "Waypoints",
            "Virus",
            "Single Virus",
            "Flag Master",
            "Bouncy Ball",
            "Chosen One",
            "Victim"
    };
    static String[] net_gamemode_descriptions = {
            "Kill other players for points",
            "Capture every waypoint to complete a lap",
            "Reach the zone before the countdown ends",
            "Bring the red flag to the blue flag",
            "Earn points for each flag held",
            "Race to the waypoint",
            "Don't catch the virus",
            "Don't catch the virus",
            "Hold onto the flag for the longest time",
            "Bounce the ball into the goal",
            "Kill the chosen one to become them",
            "Kill the victim for points"
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
                    gPlayer givePointPlayer = cGameLogic.getPlayerById(flag.get("str0"));
                    if(givePointPlayer != null) {
                        xCon.ex("givepoint " + flag.get("str0"));
                    }
                }
                cVars.putLong("kingofflagstime", uiInterface.gameTime + 1000);
            }
        }
    }
}
