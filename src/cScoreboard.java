public class cScoreboard {
    public static String createScoreMapStringServer() {
        String[] scoreFields = new String[]{"wins", "score", "kills", "ping"};
        StringBuilder scoreString = new StringBuilder();
        for(String id : nServer.scoresMap.keySet()) {
            for(String fn : scoreFields) {
                if(!nServer.scoresMap.get(id).containsKey(fn))
                    nServer.scoresMap.get(id).put(fn, 0);
            }
            scoreString.append(String.format("%s-%s-%s-%s-%s:",
                    id,
                    nServer.scoresMap.get(id).get(scoreFields[0]),
                    nServer.scoresMap.get(id).get(scoreFields[1]),
                    nServer.scoresMap.get(id).get(scoreFields[2]),
                    nServer.scoresMap.get(id).get(scoreFields[3])
            ));
        }
        return scoreString.toString();
    }

    public static String createSortedScoreMapStringServer() {
        String[] scoreFields = new String[]{"wins", "score", "kills", "ping"};
        StringBuilder scoreString = new StringBuilder();
        String[] sortedIds = new String[nServer.scoresMap.keySet().size()];
        int ic = 0;
        for(String id : nServer.scoresMap.keySet()) {
            sortedIds[ic++] = id;
            for(String fn : scoreFields) {
                if(!nServer.scoresMap.get(id).containsKey(fn))
                    nServer.scoresMap.get(id).put(fn, 0);
            }
        }
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 0; i < sortedIds.length-1; i++) {
                if(nServer.scoresMap.get(sortedIds[i]).get("score")
                        > nServer.scoresMap.get(sortedIds[i+1]).get("score")) {
                    sorted = false;
                    String tmp = sortedIds[i];
                    sortedIds[i] = sortedIds[i+1];
                    sortedIds[i+1] = tmp;
                }
            }
        }
        for(int i = 0 ; i < sortedIds.length; i++) {
            scoreString.append(String.format("%s-%s-%s-%s-%s:",
                    sortedIds[i],
                    nServer.scoresMap.get(sortedIds[i]).get(scoreFields[0]),
                    nServer.scoresMap.get(sortedIds[i]).get(scoreFields[1]),
                    nServer.scoresMap.get(sortedIds[i]).get(scoreFields[2]),
                    nServer.scoresMap.get(sortedIds[i]).get(scoreFields[3])
            ));
        }
        return scoreString.toString();
    }
}
