import java.util.HashMap;

public class cScoreboard {
    public static void resetScoresMap(HashMap<String, HashMap<String, Integer>> scoresMap) {
        HashMap<String, Integer> savedWins = new HashMap<>();
        for(String id : scoresMap.keySet()) {
            savedWins.put(id, scoresMap.get(id).get("wins"));
        }
        nServer.scoresMap = new HashMap<>();
        scoresMap = nServer.scoresMap;
        if(sSettings.net_server) {
            scoresMap.put("server", new HashMap<>());
            scoresMap.get("server").put("wins", 0);
            scoresMap.get("server").put("score", 0);
            scoresMap.get("server").put("kills", 0);
            scoresMap.get("server").put("ping", 0);
            for(String id : savedWins.keySet()) {
                if(!scoresMap.containsKey(id)) {
                    scoresMap.put(id, new HashMap<>());
                    scoresMap.get(id).put("wins", 0);
                    scoresMap.get(id).put("score", 0);
                    scoresMap.get(id).put("kills", 0);
                    scoresMap.get(id).put("ping", 0);
                }
                scoresMap.get(id).put("wins", savedWins.get(id));
            }
        }
    }

    public static String createSortedScoreMapStringServer(HashMap<String, HashMap<String, Integer>> scoresMap) {
        String[] scoreFields = new String[]{"wins", "score", "kills", "ping"};
        StringBuilder scoreString = new StringBuilder();
        String[] sortedIds = new String[scoresMap.keySet().size()];
        int ic = 0;
        for(String id : scoresMap.keySet()) {
            sortedIds[ic++] = id;
            HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
            for(String fn : scoreFields) {
                if(!scoresMapIdMap.containsKey(fn))
                    scoresMapIdMap.put(fn, 0);
            }
        }
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 0; i < sortedIds.length-1; i++) {
                if(scoresMap.get(sortedIds[i]).get("score")
                        < scoresMap.get(sortedIds[i+1]).get("score")) {
                    sorted = false;
                    String tmp = sortedIds[i];
                    sortedIds[i] = sortedIds[i+1];
                    sortedIds[i+1] = tmp;
                }
            }
        }
        for(int i = 0 ; i < sortedIds.length; i++) {
            HashMap<String, Integer> scoresMapIdMap = scoresMap.get(sortedIds[i]);
            scoreString.append(String.format("%s-%s-%s-%s-%s:",
                    sortedIds[i],
                    scoresMapIdMap.get(scoreFields[0]),
                    scoresMapIdMap.get(scoreFields[1]),
                    scoresMapIdMap.get(scoreFields[2]),
                    scoresMapIdMap.get(scoreFields[3])
            ));
        }
        return scoreString.toString();
    }

    public static boolean isTopScoreId(String id) {
        HashMap<String, HashMap<String, Integer>> scoresMap = nServer.scoresMap;
        if(scoresMap.containsKey(id)) {
            for(String otherId : scoresMap.keySet()) {
                if(!otherId.equals(id)) {
                    if(scoresMap.get(otherId).get("score") > scoresMap.get(id).get("score")) {
                        return false;
                    }
                }
            }
        }
        return scoresMap.get(id).get("score") > 0;
    }

    public static String getWinnerId() {
        HashMap<String, HashMap<String, Integer>> scoresMap = nServer.scoresMap;
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

    public static int getWinnerScore() {
        HashMap<String, HashMap<String, Integer>> scoresMap = nServer.scoresMap;
        int highestScore = 0;
        boolean pass = false;
        while (!pass) {
            pass = true;
            for(String id : scoresMap.keySet()) {
                HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
                if(scoresMapIdMap.get("score") > highestScore) {
                    pass = false;
                    highestScore = scoresMapIdMap.get("score");
                }
            }
        }
        return highestScore;
    }

    public static void addId(String id) {
        HashMap<String, HashMap<String, Integer>> scoresMap = nServer.scoresMap;
        scoresMap.put(id, new HashMap<>());
        scoresMap.get(id).put("wins", 0);
        scoresMap.get(id).put("score", 0);
        scoresMap.get(id).put("kills", 0);
        scoresMap.get(id).put("ping", 0);
    }
}
