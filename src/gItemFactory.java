import java.awt.*;
import java.util.HashMap;

public class gItemFactory {
    HashMap<String, gDoableItemReturn> itemLoadMap;
    private static gItemFactory instance = null;
    Image redFlagSprite;
    Image blueFlagSprite;
    private gItemFactory() {
        itemLoadMap = new HashMap<>();
        itemLoadMap.put("ITEM_SPAWNPOINT", new gDoableItemReturnSpawnPoint());
        itemLoadMap.put("ITEM_FLAGRED", new gDoableItemReturnFlagRed());
        itemLoadMap.put("ITEM_FLAGBLUE", new gDoableItemReturnFlagBlue());
        redFlagSprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_red.png"), 300, 300);
        blueFlagSprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_blue.png"), 300, 300);
    }

    public static gItemFactory instance() {
        if(instance == null)
            instance = new gItemFactory();
        return instance;
    }
}
