public class xComSpawnPlayerClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 3) {
            String playerId = toks[1];
            int x = Integer.parseInt(toks[2]);
            int y = Integer.parseInt(toks[3]);
            spawnPlayerDelegate(playerId, x, y, cClientLogic.scene);
            if(playerId.equals(uiInterface.uuid))
                cClientLogic.setUserPlayer(cClientLogic.getPlayerById(uiInterface.uuid));

            return "spawned player " + playerId + " at " + x + " " + y;
        }
        return "usage: spawnplayer <player_id> <x> <y>";
    }

    private void spawnPlayerDelegate(String playerId, int x, int y, gScene sceneToStore) {
        sceneToStore.getThingMap("THING_PLAYER").remove(playerId);
        sceneToStore.getThingMap("THING_BOTPLAYER").remove(playerId);
        gPlayer newPlayer = new gPlayer(x, y, eUtils.getPath("animations/player_red/a03.png"));
        newPlayer.put("id", playerId);
        sceneToStore.getThingMap("THING_PLAYER").put(playerId, newPlayer);
        if(playerId.contains("bot"))
            sceneToStore.getThingMap("THING_BOTPLAYER").put(playerId, newPlayer);
    }
}
