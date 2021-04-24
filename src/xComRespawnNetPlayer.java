public class xComRespawnNetPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String playerId = toks[1];
            gThing spawnpoint = eManager.currentMap.scene.getRandomSpawnpoint();
            if(spawnpoint != null) {
                nServer.instance().addNetCmd(String.format("spawnplayer %s %s %s", playerId,
                        spawnpoint.get("coordx"), spawnpoint.get("coordy")));
                nServer.instance().clientArgsMap.get(playerId).put("x", spawnpoint.get("coordx"));
                nServer.instance().clientArgsMap.get(playerId).put("y", spawnpoint.get("coordy"));
                return "respawned net player " + playerId;
            }
        }
        return "usage: respawnnetplayer <player_id>";
    }
}
