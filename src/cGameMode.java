import java.util.Arrays;
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

    static String[] net_gamemode_strings = {
            "Deathmatch",
            "Race",
            "Safe Zone",
            "Capture the Flag",
            "King of Flags",
            "Waypoints",
            "Virus",
            "flagmaster"
    };

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

    public static void resetVirusPlayers() {
        if(nServer.instance().clientArgsMap.containsKey("server") && nServer.instance().clientIds.size() > 0) {
            int randomClientIndex = (int) (Math.random() * nServer.instance().clientIds.size());
            nServer.instance().clientArgsMap.get("server").put("state",
                    nServer.instance().clientIds.get(randomClientIndex));
        }
    }
}
