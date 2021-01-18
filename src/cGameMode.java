import java.util.Arrays;

public class cGameMode {
    static int DEATHMATCH = 0;
    static int RACE = 1;
    static int SAFE_ZONES = 2;
    static int CAPTURE_THE_FLAG = 3;
    static int KING_OF_FLAGS = 4;
    static int WAYPOINTS = 5;
    static int VIRUS = 6;
    static int VIRUS_SINGLE = 7;
    static int FLAG_MASTER = 8;
    static int BOUNCYBALL = 9;
    static int CHOSENONE = 10;
    static int ANTI_CHOSENONE = 11;

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
            "Kill other players",
            "Complete laps",
            "Reach the zone in time",
            "Capture the red flag",
            "Touch every flag",
            "Be first to the zone",
            "Don't catch the virus",
            "Virus, but one at a time",
            "Hold the flag the longest",
            "Bounce the ball into the goal",
            "Kill the chosen one to become them",
            "Kill the chosen one for points"
    };

    public static void resetKingOfFlags() {
        int ctr = 0;
        for(gProp p : eManager.currentMap.scene.props()) {
            if(p.isInt("code", gProp.FLAGRED))
                ctr++;
        }
        String[] tmp = new String[ctr];
        Arrays.fill(tmp, "null");
        cVars.putArray("kofflagcaps", tmp);
    }

    public static void checkKingOfFlags() {
        if(sSettings.net_server) {
            if(cVars.getLong("kingofflagstime") < uiInterface.gameTime) {
//                for(String s : cVars.getArray("kofflagcaps")) {
//                    //need to fix kofflagcaps
//                    if(Integer.parseInt(s) > 0) {
//                        nServer.givePointToId();
//                        nServer.givePoint(Integer.parseInt(s)-1);
//                    }
//                }
                cVars.putLong("kingofflagstime", uiInterface.gameTime + 1000);
            }
        }
    }
}
