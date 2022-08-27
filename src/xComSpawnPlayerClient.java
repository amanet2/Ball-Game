public class xComSpawnPlayerClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 3) {
            String playerId = toks[1];
            int x = Integer.parseInt(toks[2]);
            int y = Integer.parseInt(toks[3]);
            spawnPlayerDelegate(playerId, x, y, cClientLogic.scene);
            if(playerId.equals(uiInterface.uuid)) {
                cClientVars.instance().put("userplayerid", playerId);
                gThing p = cClientLogic.getPlayerById(gCamera.argSet.get("trackingid"));
                if(p != null) {
                    xCon.ex(String.format("exec scripts/camcenter %d %d",
                            p.getInt("coordx") + p.getInt("dimw")/2 - eUtils.unscaleInt(sSettings.width)/2,
                            p.getInt("coordy") + p.getInt("dimh")/2 - eUtils.unscaleInt(sSettings.height)/2));
                }
                else
                    xCon.ex("exec scripts/freecamera");
            }

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
            newPlayer.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s",
                    nClient.instance().clientStateMap.get(playerId).get("color"),
                    newPlayer.get("pathsprite").substring(newPlayer.get("pathsprite").lastIndexOf('/')))));
        }
        sceneToStore.getThingMap("THING_PLAYER").put(playerId, newPlayer);
        if(playerId.contains("bot"))
            sceneToStore.getThingMap("THING_BOTPLAYER").put(playerId, newPlayer);
    }
}
