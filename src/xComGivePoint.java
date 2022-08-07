public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (!cServerLogic.gameover && toks.length > 1) {
            String id = toks[1];
            gPlayer givePointPlayer = cServerLogic.getPlayerById(id);
            if(givePointPlayer != null) { //player must be alive
                gScoreboard.addToScoreField(id, "score", 100);
                String color = "";
//                if(nServer.instance().clientArgsMap.containsKey(id)
//                && nServer.instance().clientArgsMap.get(id).containsKey("color"))
//                    color = nServer.instance().clientArgsMap.get(id).get("color");
                if(nServer.instance().clientArgsMap.containsKey(id)
                && nServer.instance().clientArgsMap.get(id).containsKey("color"))
                    color = nServer.instance().clientArgsMap.get(id).get("color");
                nServer.instance().addExcludingNetCmd("server", "cl_spawnpopup " + id + " $100"
                        + (color != null ? "#" + color : ""));
            }
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
