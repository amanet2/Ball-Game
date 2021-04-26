import java.awt.*;
import java.util.HashMap;

public class gItemFactory {
    HashMap<String, gDoableItemReturn> itemLoadMap;
    private static gItemFactory instance = null;
    Image flagSprite;
    Image redFlagSprite;
    Image blueFlagSprite;
    Image shotgunSprite;
    private gItemFactory() {
        itemLoadMap = new HashMap<>();
        itemLoadMap.put("ITEM_SPAWNPOINT", new gDoableItemReturnSpawnPoint());
        itemLoadMap.put("ITEM_FLAG", new gDoableItemReturnFlag());
//        itemLoadMap.put("ITEM_SHOTGUN", new gDoableItemReturnShotgun());
        itemLoadMap.put("ITEM_TELEPORTER_RED", new gDoableItemReturnTeleporterRed());
        itemLoadMap.put("ITEM_TELEPORTER_BLUE", new gDoableItemReturnTeleporterBlue());
        flagSprite = gTextures.getScaledImage(eUtils.getPath("misc/flag.png"), 300, 300);
        redFlagSprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_red.png"), 300, 300);
        blueFlagSprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_blue.png"), 300, 300);
        shotgunSprite = gTextures.getScaledImage(eUtils.getPath("misc/shotgun.png"), 200, 100);
    }

    public static gItemFactory instance() {
        if(instance == null)
            instance = new gItemFactory();
        return instance;
    }
}
