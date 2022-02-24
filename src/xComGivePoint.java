public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (cVars.isZero("gameover") && toks.length > 1) {
            String id = toks[1];
            gPlayer givePointPlayer = cServerLogic.getPlayerById(id);
            if(givePointPlayer != null) { //player must be alive
                gScoreboard.addToScoreField(id, "score", 100);
                nServer.instance().addExcludingNetCmd("server",
                        "cl_spawnpopup " + givePointPlayer.get("id") + " +100");
            }
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
