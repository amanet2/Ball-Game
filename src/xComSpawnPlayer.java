public class xComSpawnPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 3) {
            String playerId = toks[1];
            int x = Integer.parseInt(toks[2]);
            int y = Integer.parseInt(toks[3]);
            eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(playerId);
            eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").remove(playerId);
            gPlayer newPlayer = new gPlayer(x, y, eUtils.getPath("animations/player_red/a03.png"));
            newPlayer.put("id", playerId);
            eManager.currentMap.scene.getThingMap("THING_PLAYER").put(playerId, newPlayer);
            if(playerId.contains("bot"))
                eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").put(playerId, newPlayer);
            if(playerId.equals(uiInterface.uuid))
                gClientLogic.setUserPlayer(newPlayer);
            return "spawned player " + playerId + " at " + x + " " + y;
        }
        return "usage: spawnplayer <player_id> <x> <y>";
    }
}
