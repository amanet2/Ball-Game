import java.util.HashMap;

public class gScoreboard {
    static HashMap<String, HashMap<String, Integer>> scoresMap = new HashMap<>(); //server too, index by uuids

    public static void resetScoresMap() {
        HashMap<String, Integer> savedWins = new HashMap<>();
        for(String id : scoresMap.keySet()) {
            savedWins.put(id, scoresMap.get(id).get("wins"));
        }
        scoresMap = new HashMap<>();
        for(String id : savedWins.keySet()) {
            if(!scoresMap.containsKey(id)) {
                addId(id);
            }
            scoresMap.get(id).put("wins", savedWins.get(id));
        }
    }

    public static boolean isTopScoreId(String id) {
        if(scoresMap.containsKey(id)) {
            for(String otherId : scoresMap.keySet()) {
                if(!otherId.equals(id)) {
                    if(scoresMap.get(otherId).get("score") > scoresMap.get(id).get("score")) {
                        return false;
                    }
                }
            }
            return scoresMap.get(id).get("score") > 0;
        }
        return false;
    }

    public static String getTopScoreString() {
        int topscore = 0;
        int tiectr = 0;
        String leaderString = "";
        for(String id : scoresMap.keySet()) {
            HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
            if(scoresMapIdMap.get("score") > topscore) {
                tiectr = 0;
                topscore = scoresMapIdMap.get("score");
                if(!nServer.instance().clientArgsMap.containsKey(id)
                || !nServer.instance().clientArgsMap.get(id).containsKey("name"))
                    break;
                leaderString = nServer.instance().clientArgsMap.get(id).get("name") + " ("+topscore+")";
            }
            else if(topscore > 0 && scoresMapIdMap.get("score") == topscore) {
                tiectr++;
            }
        }
        if(tiectr > 0) {
            leaderString = leaderString + " + " + tiectr + " others";
        }
        return leaderString;
    }

    public static String getWinnerId() {
        int highestScore = 0;
        String highestId = "";
        boolean pass = false;
        while (!pass) {
            pass = true;
            for(String id : scoresMap.keySet()) {
                HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
                if(scoresMapIdMap.get("score") > highestScore) {
                    pass = false;
                    highestId = id;
                    highestScore = scoresMapIdMap.get("score");
                }
            }
        }
        return highestId;
    }

    public static void addId(String id) {
        scoresMap.put(id, new HashMap<>());
        scoresMap.get(id).put("wins", 0);
        scoresMap.get(id).put("score", 0);
//        scoresMap.get(id).put("kills", 0);
//        scoresMap.get(id).put("ping", 0);
    }

    public static void incrementScoreFieldById(String id, String field) {
        if(!scoresMap.containsKey(id))
            scoresMap.put(id, new HashMap<>());
        HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
        if(!scoresMapIdMap.containsKey(field))
            scoresMapIdMap.put(field, 0);
        int nscore = scoresMapIdMap.get(field) + 1;
        scoresMapIdMap.put(field, nscore);
    }

    public static void addToScoreField(String id, String field, int score) {
        if(!scoresMap.containsKey(id))
            scoresMap.put(id, new HashMap<>());
        HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
        if(!scoresMapIdMap.containsKey(field))
            scoresMapIdMap.put(field, 0);
        int nscore = scoresMapIdMap.get(field) + score;
        if(nscore < 0)
            nscore = 0;
        scoresMapIdMap.put(field, nscore);
    }
}
