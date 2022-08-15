import java.awt.*;
import java.util.HashMap;

public class gItemFactory {
    HashMap<String, gDoableThingReturn> itemLoadMap;
    private static gItemFactory instance = null;
    Image flagSprite;
    private gItemFactory() {
        itemLoadMap = new HashMap<>();
        itemLoadMap.put("ITEM_SPAWNPOINT", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                return new gItem("ITEM_SPAWNPOINT", Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        300, 300, null);
            }
        });
        itemLoadMap.put("ITEM_FLAG", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gItem item = new gItem("ITEM_FLAG", Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        200, 300, gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"),
                        200, 300));
                item.put("script", "exec items/flag");
                return item;
            }
        });
        itemLoadMap.put("ITEM_LANDMINE", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gItem item = new gItem("ITEM_LANDMINE", Integer.parseInt(args[0]), Integer.parseInt(args[1]), 300, 300,
                        gTextures.getGScaledImage(eUtils.getPath("misc/forbidden.png"), 300, 300));
                item.put("script", "exec items/landmine");
                return item;
            }
        });
        itemLoadMap.put("ITEM_POINTGIVER", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gItem item = new gItem("ITEM_POINTGIVER", Integer.parseInt(args[0]), Integer.parseInt(args[1]), 300, 300,
                        gTextures.getGScaledImage(eUtils.getPath("misc/light1.png"), 300, 300));
                item.put("script", "exec items/pointgiver");
                return item;
            }
        });
        itemLoadMap.put("ITEM_TELEPORTER_RED", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gItem item = new gItem("ITEM_TELEPORTER_RED", Integer.parseInt(args[0]), Integer.parseInt(args[1]), 300, 300,
                        gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_red.png"), 300, 300));
                item.put("script", "exec items/teleporterred");
                return item;
            }
        });
        itemLoadMap.put("ITEM_TELEPORTER_BLUE", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gItem item = new gItem("ITEM_TELEPORTER_BLUE", Integer.parseInt(args[0]), Integer.parseInt(args[1]), 300, 300,
                        gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_blue.png"), 300, 300));
                item.put("script", "exec items/teleporterblue");
                return item;
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
