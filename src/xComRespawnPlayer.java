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
                player.put("stockhp", cVars.get("maxstockhp"));
                int randomSpawnpointIndex = new Random().nextInt(
                        eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").size());
                ArrayList<String> spawnpointids =
                        new ArrayList<>(eManager.currentMap.scene.getThingMap("ITEM_SPAWNPOINT").keySet());
                String randomId = spawnpointids.get(randomSpawnpointIndex);
                gItemSpawnPoint spawnpoint =
                        (gItemSpawnPoint) eManager.currentMap.scene.getThingMap(
                                "ITEM_SPAWNPOINT").get(randomId);
                //server-side solution
                if(!id.contains("bot")) {
                    nServer.instance().addNetCmd(id, "createuserplayer;userplayer coordx " +
                            (spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2
                                    - player.getInt("dimw") / 2)
                            + ";userplayer coordy " + (spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2
                            - player.getInt("dimh") / 2));
                    nServer.instance().addNetCmd(id, "cv_camplayertrackingid " + id + ";centercamera");
                }
                else {
                    player.putInt("coordx", (spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw") / 2
                            - player.getInt("dimw") / 2));
                    player.putInt("coordy", (spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh") / 2
                            - player.getInt("dimh") / 2));
                    nServer.instance().addExcludingNetCmd("server", "respawnclientbotplayer " + id);
                }
                return "respawned " + id;
            }
            else if(nServer.instance().clientArgsMap.containsKey(id)
            && nServer.instance().clientArgsMap.get(id).containsKey("name")) {
                if(!id.contains("bot")) {
                    if(id.equals(uiInterface.uuid)) {
                        xCon.ex("createuserplayer;respawn");
                    }
                    else {
                        nServer.instance().createServersidePlayer(id, nServer.instance().clientArgsMap.get(id).get("name"));
                        doCommand(fullCommand);
                    }
                }
                else {
                    String botcolor = nServer.instance().clientArgsMap.get(id).get("color");
                    String bothat = nServer.instance().clientArgsMap.get(id).get("hat");
                    gPlayer p = new gPlayer(-6000,-6000,
                            eUtils.getPath(String.format("animations/player_%s/a03.png", botcolor)));
                    p.put("id", id);
                    p.put("hat", bothat);
                    eManager.currentMap.scene.playersMap().put(p.get("id"), p);
                    eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").put(p.get("id"), p);
                    nVarsBot.update(p);
                    doCommand(fullCommand);
                }
                return "created player for id: " + id;
            }
            else {
                return "can't respawn player id: " + id;
            }
        }
        return "usage: respawnplayer <player_id>";
    }
}
