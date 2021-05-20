public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (cVars.isZero("gameover") && toks.length > 1) {
            String id = toks[1];
            if(cServerLogic.getPlayerById(id) != null) //player must be alive
            gScoreboard.incrementScoreFieldById(id, "score");
//            gPlayer givePointPlayer = cServerLogic.getPlayerById(id);
//            if(givePointPlayer != null)
//                nServer.instance().addExcludingNetCmd("server",
//                        "cl_spawnpopup " + givePointPlayer.get("id") + " +1");
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
