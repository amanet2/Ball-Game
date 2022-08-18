import java.util.ArrayList;
import java.util.Random;

public class xComSpawnPointgiver extends xCom {
    public String doCommand(String fullCommand) {
        gThing spawnpoint = cServerLogic.getRandomThing("ITEM_SPAWNPOINT");
        if(spawnpoint == null)
            return "could not get random spawnpoint";
        int itemId = 0;
        for(String id : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {;
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        itemId++; //want to be the _next_ id
        xCon.ex(String.format("exec scripts/spawnpointgiver %d %d %d", itemId,
                spawnpoint.getInt("coordx"), spawnpoint.getInt("coordy")));
        return "spawned pointgiver";
    }
}
