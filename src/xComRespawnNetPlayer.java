public class xComRespawnNetPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String playerId = toks[1];
            gThing spawnpoint = cServerLogic.getRandomSpawnpoint();
            if(spawnpoint != null) {
                nServer.instance().clientArgsMap.get(playerId).put("x", spawnpoint.get("coordx"));
                nServer.instance().clientArgsMap.get(playerId).put("y", spawnpoint.get("coordy"));
                String cmdstring = String.format("spawnplayer %s %s %s",
                        playerId, spawnpoint.get("coordx"), spawnpoint.get("coordy"));
                xCon.ex(cmdstring);
                nServer.instance().addExcludingNetCmd("server",
                        cmdstring.replaceFirst("spawnplayer", "cl_spawnplayer"));
                return "respawned net player " + playerId;
            }
        }
        return "usage: respawnnetplayer <player_id>";
    }
}
