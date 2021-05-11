import java.util.*;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    private static final String[] object_titles = new String[]{
        "THING_PLAYER","THING_BULLET","THING_POPUP","THING_FLARE","THING_ANIMATION", "THING_BOTPLAYER", "THING_BLOCK",
        "BLOCK_CUBE", "BLOCK_FLOOR", "BLOCK_CORNERUR", "BLOCK_CORNERLR", "BLOCK_CORNERLL", "BLOCK_CORNERUL",
        "THING_COLLISION", "THING_ITEM", "ITEM_SPAWNPOINT", "ITEM_FLAGRED", "ITEM_FLAGBLUE", "ITEM_SHOTGUN",
        "ITEM_TELEPORTER_RED", "ITEM_TELEPORTER_BLUE", "ITEM_FLAG"
    };

	HashMap<String, LinkedHashMap> objectMaps;
	int blockIdCtr;
	int collisionIdCtr;
	int itemIdCtr;
	int flareIdCtr;

	public gScene() {
        objectMaps = new HashMap<>();
        for(String s : object_titles) {
            objectMaps.put(s, new LinkedHashMap());
        }
        blockIdCtr = 0;
        collisionIdCtr = 0;
        itemIdCtr = 0;
        flareIdCtr = 0;
    }

    public LinkedHashMap<String, gThing> getThingMap(String thing_title) {
	    return objectMaps.get(thing_title);
    }

    public gPlayer getPlayerById(String id) {
        return (gPlayer) getThingMap("THING_PLAYER").get(id);
    }
}