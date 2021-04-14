import java.util.ArrayList;
import java.util.Random;

public class xComRespawn extends xCom {
    public String doCommand(String fullCommand) {
        ArrayList<String> spawnpointids =
                new ArrayList<>(eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").keySet());
        gPlayer userPlayer = cGameLogic.userPlayer();
        if(userPlayer == null)
            return "no userplayer to respawn";
        if(spawnpointids.size() > 0) {
            int randomSpawnpointIndex = new Random().nextInt(
                    eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").size());
            String randomId = spawnpointids.get(randomSpawnpointIndex);
            gItemSpawnPoint spawnpoint =
                    (gItemSpawnPoint) eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").get(randomId);
            //player-centric spawn comands
            userPlayer.putInt("coordx", spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2
                    - cGameLogic.userPlayer().getInt("dimw") / 2);
            userPlayer.putInt("coordy", spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2
                    - cGameLogic.userPlayer().getInt("dimh") / 2);
        }
        userPlayer.put("stockhp", cVars.get("maxstockhp"));
        xCon.ex("cv_flashlight 0");
        xCon.ex("cv_sprint 0");
        xCon.ex("cv_stockspeed cv_maxstockspeed");
        cVars.put("camplayertrackingid", uiInterface.uuid);
        cScripts.centerCamera();
        return "respawned";
    }
}
