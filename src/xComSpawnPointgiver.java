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
                String cmd = String.format("putitem ITEM_POINTGIVER %d %d %d", itemId, nx, ny);
            xCon.ex(cmd);
            nServer.instance().addExcludingNetCmd("server",
                    cmd.replaceFirst("putitem", "cl_putitem"));
            return "spawned pointgiver";
        }
        return "usage: spawnpointgiver";
    }
}
