public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            if (cVars.isOne("gameteam")) {
                String color = cGameLogic.getPlayerById(id).get("color");
                for (String mapid : nServer.scoresMap.keySet()) {
                    if (color.equals(cGameLogic.getPlayerById(mapid).get("color"))) {
                        nServer.incrementScoreFieldById(mapid, "score");
                    }
                }
            }
            else {
                nServer.incrementScoreFieldById(id, "score");
            }
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
