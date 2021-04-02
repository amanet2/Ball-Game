import java.util.HashMap;

public class gItemFactory {
    HashMap<String, gDoableItemReturn> itemLoadMap;
    private static gItemFactory instance = null;

    private gItemFactory() {
        itemLoadMap = new HashMap<>();
        itemLoadMap.put("ITEM_SPAWNPOINT", new gDoableItemReturnSpawnPoint());
        itemLoadMap.put("ITEM_FLAGRED", new gDoableItemReturnFlagRed());
    }

    public static gItemFactory instance() {
        if(instance == null)
            instance = new gItemFactory();
        return instance;
    }
}
