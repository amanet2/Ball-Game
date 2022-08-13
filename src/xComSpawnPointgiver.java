import java.util.ArrayList;
import java.util.Random;

public class xComSpawnPointgiver extends xCom {
    public String doCommand(String fullCommand) {
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
            int nx = spawnpoint.getInt("coordx") + spawnpoint.getInt("dimw")/2 - 100;
            int ny = spawnpoint.getInt("coordy") + spawnpoint.getInt("dimh")/2 - 100;
            int itemId = 0;
            for(String id : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {;
                if(itemId < Integer.parseInt(id))
                    itemId = Integer.parseInt(id);
            }
            itemId++; //want to be the _next_ id
            xCon.ex(String.format("exec scripts/spawnpointgiver %d %d %d", itemId, nx, ny));
            return "spawned pointgiver";
        }
        return "usage: spawnpointgiver";
    }
}
