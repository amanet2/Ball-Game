import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class xComRespawn extends xCom {
    public String doCommand(String fullCommand) {
        int randomSpawnpointIndex = ThreadLocalRandom.current().nextInt(0,
                eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
        ArrayList<String> spawnpointids = new ArrayList<>();
        for(String id : eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").keySet()) {
            spawnpointids.add(id);
        }
        String randomId = spawnpointids.get(randomSpawnpointIndex);
        gPropSpawnpoint spawnpoint =
                (gPropSpawnpoint) eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").get(randomId);

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


//        int canspawn = cScripts.canSpawnPlayer() ? 1 : 0;
//        cVars.put("onladder", "0");
//        cVars.put("inboost", "0");
//        if(canspawn != 0) {
//            while(true) {
//                int randomNum = ThreadLocalRandom.current().nextInt(0,
//                    eManager.currentMap.scene.tiles().size());
//                gTile t = eManager.currentMap.scene.tiles().get(randomNum);
//                if(t.isOne("canspawn") && !cGameLogic.userPlayer().willCollideWithinTileAtCoords(t,
//                    t.getInt("coordx") + t.getInt("dimw")/2 - cGameLogic.userPlayer().getInt("dimw")/2,
//                    t.getInt("coordy") + t.getInt("dimh")/2 - cGameLogic.userPlayer().getInt("dimh")/2)) {
//                    boolean pass = true;
//                    for (gPlayer target : eManager.currentMap.scene.players()) {
//                        if (target.getInt("tag") != cGameLogic.userPlayer().getInt("tag") &&
//                                cGameLogic.userPlayer().willCollideWithPlayerAtCoords(target, t.getInt("coordx"),
//                                        t.getInt("coordy"))) {
//                            pass = false;
//                            break;
//                        }
//                    }
//                    if(pass) {
//                        xCon.ex("THING_PLAYER.0.coordx " + (t.getInt("coordx") + t.getInt("dimw") / 2
//                                - cGameLogic.userPlayer().getInt("dimw") / 2));
//                        xCon.ex("THING_PLAYER.0.coordy "+ (t.getInt("coordy") + t.getInt("dimh") / 2
//                                - cGameLogic.userPlayer().getInt("dimh") / 2));
//                        if(cVars.getInt("gamespawnarmed") < 1) {
//                            cScripts.clearWeaponStocks();
//                        }
//                        else {
//                            cScripts.refillWeaponStocks();
//                        }
//                        cVars.put("stockhp", cVars.get("maxstockhp"));
//                        cGameLogic.userPlayer().put("alive", "1");
//                        xCon.ex("cv_flashlight 0");
//                        xCon.ex("cv_sprint 0");
//                        xCon.ex("cv_stockspeed cv_maxstockspeed");
//                        cVars.put("camplayertrackingid", sSettings.net_server ? "server" : uiInterface.uuid);
//                        xCon.ex("centercamera");
//                        if(cVars.contains("spawnprotectionmaxtime") && cVars.getInt("spawnprotectionmaxtime") > 0) {
//                            cVars.putLong("spawnprotectiontime",
//                                    System.currentTimeMillis() + cVars.getInt("spawnprotectionmaxtime"));
//                        }
//                        break;
//                    }
//                }
//            }
//        }
        return "respawned";
    }
}
