import java.util.ArrayList;
import java.util.Random;

public class xComRespawnNetPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String playerId = toks[1];
            gItemSpawnPoint spawnpoint = null;
            int size = cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").size();
            if(size > 0) {
                int randomSpawnpointIndex = new Random().nextInt(size);
                ArrayList<String> spawnpointids =
                        new ArrayList<>(cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").keySet());
                String randomId = spawnpointids.get(randomSpawnpointIndex);
                spawnpoint = (gItemSpawnPoint) cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").get(randomId);
                while(spawnpoint.isOccupied()) {
                    randomSpawnpointIndex = new Random().nextInt(size);
                    randomId = spawnpointids.get(randomSpawnpointIndex);
                    spawnpoint = (gItemSpawnPoint) cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").get(randomId);
                }
            }
            if(spawnpoint != null) {
                xCon.ex(String.format("exec scripts/respawnnetplayer %s %s", playerId, spawnpoint.get("id")));
                return "respawned net player " + playerId;
            }
    }
        return "usage: respawnnetplayer <player_id>";
    }
}
