import java.util.ArrayList;
import java.util.Random;

public class xComRespawn extends xCom {
    public String doCommand(String fullCommand) {
        ArrayList<String> spawnpointids =
                new ArrayList<>(eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").keySet());
        cGameLogic.setUserPlayer(null);
        eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(uiInterface.uuid);
        if(spawnpointids.size() > 0) {
            int randomSpawnpointIndex = new Random().nextInt(
                    eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").size());
            String randomId = spawnpointids.get(randomSpawnpointIndex);
            gItemSpawnPoint spawnpoint =
                    (gItemSpawnPoint) eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").get(randomId);
            //player-centric spawn comands
            gPlayer player0 = new gPlayer(
                    spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2 - 100,
                    spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2 - 100,
                    eUtils.getPath(String.format("animations/player_%s/a03.png", sVars.get("playercolor"))));
            player0.put("id", uiInterface.uuid);
            player0.put("color", sVars.get("playercolor"));
            player0.put("stockhp", cVars.get("maxstockhp"));
            eManager.currentMap.scene.playersMap().put(player0.get("id"), player0);
            cGameLogic.setUserPlayer(player0);
            cVars.put("camplayertrackingid", uiInterface.uuid);
            cScripts.centerCamera();
        }
        return "respawned";
    }
}
