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


        //player-centric spawn comands
        xCon.ex("THING_PLAYER.0.coordx " + (spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2
                - cGameLogic.userPlayer().getInt("dimw") / 2));
        xCon.ex("THING_PLAYER.0.coordy "+ (spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2
                - cGameLogic.userPlayer().getInt("dimh") / 2));
        if(cVars.getInt("gamespawnarmed") < 1) {
            cScripts.clearWeaponStocks();
        }
        else {
            cScripts.refillWeaponStocks();
        }
        cVars.put("stockhp", cVars.get("maxstockhp"));
        cGameLogic.userPlayer().put("alive", "1");
        xCon.ex("cv_flashlight 0");
        xCon.ex("cv_sprint 0");
        xCon.ex("cv_stockspeed cv_maxstockspeed");
        cVars.put("camplayertrackingid", sSettings.net_server ? "server" : uiInterface.uuid);
        xCon.ex("centercamera");
        if(cVars.contains("spawnprotectionmaxtime") && cVars.getInt("spawnprotectionmaxtime") > 0) {
            cVars.putLong("spawnprotectiontime",
                    System.currentTimeMillis() + cVars.getInt("spawnprotectionmaxtime"));
        }
        return "respawned";
    }
}
