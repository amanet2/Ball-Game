public class xComSpawnPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 3) {
            String playerId = toks[1];
            int x = Integer.parseInt(toks[2]);
            int y = Integer.parseInt(toks[3]);
            if(sSettings.IS_SERVER) {
                gScene scene = cServerLogic.scene;
                scene.getThingMap("THING_PLAYER").remove(playerId);
                scene.getThingMap("THING_BOTPLAYER").remove(playerId);
                gPlayer newPlayer = new gPlayer(x, y, eUtils.getPath("animations/player_red/a03.png"));
                newPlayer.put("id", playerId);
                scene.getThingMap("THING_PLAYER").put(playerId, newPlayer);
                if(playerId.contains("bot"))
                    scene.getThingMap("THING_BOTPLAYER").put(playerId, newPlayer);
            }
            if(sSettings.IS_CLIENT) {
                gScene scene = cClientLogic.scene;
                scene.getThingMap("THING_PLAYER").remove(playerId);
                scene.getThingMap("THING_BOTPLAYER").remove(playerId);
                gPlayer newPlayer = new gPlayer(x, y, eUtils.getPath("animations/player_red/a03.png"));
                newPlayer.put("id", playerId);
                scene.getThingMap("THING_PLAYER").put(playerId, newPlayer);
                if(playerId.contains("bot"))
                    scene.getThingMap("THING_BOTPLAYER").put(playerId, newPlayer);
                if(playerId.equals(uiInterface.uuid))
                    cClientLogic.setUserPlayer(newPlayer);
            }

            return "spawned player " + playerId + " at " + x + " " + y;
        }
        return "usage: spawnplayer <player_id> <x> <y>";
    }
}
