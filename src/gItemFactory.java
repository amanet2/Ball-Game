import java.awt.*;
import java.util.HashMap;

public class gItemFactory {
    HashMap<String, gDoableItemReturn> itemLoadMap;
    private static gItemFactory instance = null;
    Image flagSprite;
    private gItemFactory() {
        itemLoadMap = new HashMap<>();
        itemLoadMap.put("ITEM_SPAWNPOINT", new gDoableItemReturnSpawnPoint());
        itemLoadMap.put("ITEM_FLAG", new gDoableItemReturnFlag());
        itemLoadMap.put("ITEM_LANDMINE", new gDoableItemReturnLandmine());
        itemLoadMap.put("ITEM_POINTGIVER", new gDoableItemReturnPointGiver());
        itemLoadMap.put("ITEM_TELEPORTER_RED", new gDoableItemReturnTeleporterRed());
        itemLoadMap.put("ITEM_TELEPORTER_BLUE", new gDoableItemReturnTeleporterBlue());
        flagSprite = gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"), 200, 300);
    }

    public static gItemFactory instance() {
        if(instance == null)
            instance = new gItemFactory();
        return instance;
    }
}
