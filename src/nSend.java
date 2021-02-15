import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class nSend {
    static String focus_id = ""; //for when you need to operate on a response to a specific id
    static HashMap<String, String> sendMap = null;
    static String[] constantFields = {"map", "mode", "teams", "armed", "tick", "powerups", "scoremap", "scorelimit",
            "timeleft", "timelimit", "topscore", "state", "win", "vels", "dirs", "x", "y", "msg", "weapon",
            "spawnprotectionmaxtime"};
    static List<String> constantsList = Arrays.asList(constantFields);
}
