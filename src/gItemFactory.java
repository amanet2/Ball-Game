import java.awt.*;
import java.util.HashMap;

public class gItemFactory {
    HashMap<String, gDoableItemReturn> itemLoadMap;
    private static gItemFactory instance = null;
    Image flagSprite;
    private gItemFactory() {
        itemLoadMap = new HashMap<>();
        itemLoadMap.put("ITEM_SPAWNPOINT", new gDoableItemReturn() {
            public gItem getItem(String[] args) {
                return new gItem("ITEM_SPAWNPOINT", Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        300, 300, null);
            }
        });
        itemLoadMap.put("ITEM_FLAG", new gDoableItemReturn() {
            public gItem getItem(String[] args) {
                return new gItemFlag(
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])
                );
            }
        });
        itemLoadMap.put("ITEM_LANDMINE", new gDoableItemReturn() {
            public gItem getItem(String[] args) {
                return new gItemLandmine(
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])
                );
            }
        });
        itemLoadMap.put("ITEM_POINTGIVER", new gDoableItemReturn() {
            public gItem getItem(String[] args) {
                return new gItemPointGiver(
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])
                );
            }
        });
        itemLoadMap.put("ITEM_TELEPORTER_RED", new gDoableItemReturn() {
            public gItem getItem(String[] args) {
                return new gItemTeleporterRed(
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])
                );
            }
        });
        itemLoadMap.put("ITEM_TELEPORTER_BLUE", new gDoableItemReturn() {
            public gItem getItem(String[] args) {
                return new gItemTeleporterBlue(
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])
                );
            }
        });
        flagSprite = gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"), 200, 300);
    }

    public static gItemFactory instance() {
        if(instance == null)
            instance = new gItemFactory();
        return instance;
    }
}
