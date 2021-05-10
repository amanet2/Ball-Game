public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            gScoreboard.incrementScoreFieldById(id, "score");
            gPlayer givePointPlayer = gScene.getPlayerById(id);
            if(givePointPlayer != null)
                nServer.instance().addExcludingNetCmd("server",
                        "spawnpopup " + givePointPlayer.get("id") + " 1");
//                nServer.instance().addNetCmd("echo " + nServer.instance().clientArgsMap.get(id).get("name") + " scored");
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
