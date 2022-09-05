public class xComSpawnPlayerClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 3) {
            String playerId = toks[1];
            int x = Integer.parseInt(toks[2]);
            int y = Integer.parseInt(toks[3]);
            spawnPlayerDelegate(playerId, x, y, cClientLogic.scene);
            if(playerId.equals(uiInterface.uuid))
                xCon.ex("cl_setvar userplayerid $userid");
            return "spawned player " + playerId + " at " + x + " " + y;
        }
        return "usage: spawnplayer <player_id> <x> <y>";
    }

    private void spawnPlayerDelegate(String playerId, int x, int y, gScene sceneToStore) {
        sceneToStore.getThingMap("THING_PLAYER").remove(playerId);
        sceneToStore.getThingMap("THING_BOTPLAYER").remove(playerId);
        gPlayer newPlayer = new gPlayer(playerId, x, y, cClientLogic.maxhp,
                eUtils.getPath("animations/player_red/a03.png"));
        if(nClient.instance().clientStateMap.contains(playerId)) {
            newPlayer.put("color", nClient.instance().clientStateMap.get(playerId).get("color"));
            newPlayer.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a03.png",
                    nClient.instance().clientStateMap.get(playerId).get("color"))));
        }
        sceneToStore.getThingMap("THING_PLAYER").put(playerId, newPlayer);
        if(playerId.contains("bot"))
            sceneToStore.getThingMap("THING_BOTPLAYER").put(playerId, newPlayer);
    }
}
