import java.util.ArrayList;
import java.util.Random;

public class xComRespawnPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            gPlayer player = gScene.getPlayerById(id);
            if(player != null) {
                //respawn code here
//                player.put("stockhp", cVars.get("maxstockhp"));
                int randomSpawnpointIndex = new Random().nextInt(
                        eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
                ArrayList<String> spawnpointids =
                        new ArrayList<>(eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").keySet());
                String randomId = spawnpointids.get(randomSpawnpointIndex);
                gPropSpawnpoint spawnpoint =
                        (gPropSpawnpoint) eManager.currentMap.scene.getThingMap(
                                "PROP_SPAWNPOINT").get(randomId);
                //server-side solution
                nServer.instance().addNetCmd(id, "userplayer coordx " +
                        (spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2
                        - cGameLogic.userPlayer().getInt("dimw") / 2)
                + ";userplayer coordy " + (spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2
                        - cGameLogic.userPlayer().getInt("dimh") / 2));
                nServer.instance().addNetCmd(id, "cv_flashlight 0;cv_sprint 0;cv_stockspeed cv_maxstockspeed;" +
                        "cv_camplayertrackingid " + id + ";centercamera");
                //player-centric spawn comands
//                cScripts.refillWeaponStocks();
                player.remove("respawntime");
                if(cVars.contains("spawnprotectionmaxtime") && cVars.getInt("spawnprotectionmaxtime") > 0) {
                    player.putLong("spawnprotectiontime",
                            System.currentTimeMillis() + cVars.getInt("spawnprotectionmaxtime"));
                }
            }
            return "respawned " + id;
        }
        return "usage: respawnplayer <player_id>";
    }
}
