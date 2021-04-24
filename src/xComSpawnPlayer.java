public class xComSpawnPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 3) {
            String playerId = toks[1];
            int x = Integer.parseInt(toks[2]);
            int y = Integer.parseInt(toks[3]);
            gPlayer newPlayer = new gPlayer(x, y, eUtils.getPath("animations/player_red/a03.png"));
            newPlayer.put("id", playerId);
            eManager.currentMap.scene.getThingMap("THING_PLAYER").put(playerId, newPlayer);
            return "spawned player " + playerId + " at " + x + " " + y;
        }
        return "usage: spawnplayer <player_id> <x> <y>";
    }
}
