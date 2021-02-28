import java.util.Arrays;
import java.util.HashMap;

public class cScoreboard {
    static HashMap<String, HashMap<String, Integer>> scoresMap = new HashMap<>(); //server too, index by uuids

    public static void resetScoresMap() {
        HashMap<String, Integer> savedWins = new HashMap<>();
        for(String id : scoresMap.keySet()) {
            savedWins.put(id, scoresMap.get(id).get("wins"));
        }
        scoresMap = new HashMap<>();
        if(sSettings.net_server) {
            addId("server");
            for(String id : savedWins.keySet()) {
                if(!scoresMap.containsKey(id)) {
                    addId(id);
                }
                scoresMap.get(id).put("wins", savedWins.get(id));
            }
        }
    }

    public static String createSortedScoreMapStringServer() {
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
        String winnerName = "";
        if(cVars.isZero("gameteam")) {
            for(String id : scoresMap.keySet()) {
                HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
                if(scoresMapIdMap.get("score") > topscore) {
                    tiectr = 0;
                    topscore = scoresMapIdMap.get("score");
                    winnerName = nServer.instance().clientArgsMap.get(id).get("name") + " ("+topscore+")";
                }
                else if(topscore > 0 && scoresMapIdMap.get("score") == topscore) {
                    tiectr++;
                }
            }
            if(tiectr > 0) {
                winnerName = winnerName + " + " + tiectr + " others";
            }
        }
        else {
//            String[] colors = sVars.getArray("colorselection");
//            int[] colorscores = new int[colors.length];
//            Arrays.fill(colorscores, 0);
//            for(String id : scoresMap.keySet()) {
//                gPlayer p = gScene.getPlayerById(id);
//                for(int j = 0; j < colors.length; j++) {
//                    if(p.get("color").equals(colors[j])) {
//                        colorscores[j] = scoresMap.get(id).get("score");
//                    }
//                }
//            }
//            for(int i = 0; i < colorscores.length; i++) {
//                if(colorscores[i] > topscore) {
//                    topscore = colorscores[i];
//                    winnerName = colors[i] + " (" + colorscores[i]+")";
//                }
//            }
        }
        return winnerName;
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

    public static int getWinnerScore() {
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
        scoresMap.put(id, new HashMap<>());
        scoresMap.get(id).put("wins", 0);
        scoresMap.get(id).put("score", 0);
        scoresMap.get(id).put("kills", 0);
        scoresMap.get(id).put("ping", 0);
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
}
