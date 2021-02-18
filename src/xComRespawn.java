import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class xComRespawn extends xCom {
    public String doCommand(String fullCommand) {
        int randomSpawnpointIndex = ThreadLocalRandom.current().nextInt(0,
                eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
        ArrayList<String> spawnpointids =
                new ArrayList<>(eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").keySet());
        String randomId = spawnpointids.get(randomSpawnpointIndex);
        gPropSpawnpoint spawnpoint =
                (gPropSpawnpoint) eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").get(randomId);
        gPlayer userPlayer = cGameLogic.userPlayer();
        //player-centric spawn comands
        userPlayer.putInt("coordx", spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2
                - cGameLogic.userPlayer().getInt("dimw") / 2);
        userPlayer.putInt("coordy", spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2
                - cGameLogic.userPlayer().getInt("dimh") / 2);
        cScripts.refillWeaponStocks();
        userPlayer.put("stockhp", cVars.get("maxstockhp"));
        userPlayer.remove("respawntime");
        xCon.ex("cv_flashlight 0");
        xCon.ex("cv_sprint 0");
        xCon.ex("cv_stockspeed cv_maxstockspeed");
        cVars.put("camplayertrackingid", sSettings.net_server ? "server" : uiInterface.uuid);
        xCon.ex("centercamera");
        if(cVars.contains("spawnprotectionmaxtime") && cVars.getInt("spawnprotectionmaxtime") > 0) {
            userPlayer.putLong("spawnprotectiontime",
                    System.currentTimeMillis() + cVars.getInt("spawnprotectionmaxtime"));
        }
        return "respawned";
    }
}
