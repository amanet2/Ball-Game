import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    static final String[] object_titles = new String[]{
        "THING_TILE","THING_PLAYER","THING_BULLET","THING_POPUP","THING_PROP","THING_FLARE","THING_ANIMATION",
            "THING_BOTPLAYER", "PROP_TELEPORTER", "PROP_SCOREPOINT", "PROP_BOOST", "PROP_FLAGBLUE",
            "PROP_FLAGRED", "PROP_POWERUP", "PROP_SPAWNPOINT", "THING_BLOCK", "BLOCK_CUBE", "BLOCK_FLOOR",
            "BLOCK_CORNERUR", "BLOCK_CORNERLR", "BLOCK_CORNERLL", "BLOCK_CORNERUL", "THING_PREFAB", "PREFAB_CAGE",
            "PREFAB_CAGEB", "THING_COLLISION", "THING_ITEM", "ITEM_SPAWNPOINT", "ITEM_FLAGRED", "ITEM_FLAGBLUE",
            "ITEM_SHOTGUN", "ITEM_TELEPORTER_RED", "ITEM_TELEPORTER_BLUE", "ITEM_FLAG"
    };

	HashMap<String, ArrayList> objectLists;
	HashMap<String, HashMap> objectMaps;
	int blockIdCtr;
	int collisionIdCtr;
	int itemIdCtr;

	public gScene() {
		objectLists = new HashMap<>();
		objectMaps = new HashMap<>();
		for(String s : object_titles) {
			objectLists.put(s, new ArrayList<>());
            objectMaps.put(s, new HashMap<>());
            blockIdCtr = 0;
            collisionIdCtr = 0;
        }
	}

	public int getHighestPrefabId() {
	    int idctr = 0;
	    for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
	        gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
	        if(block.contains("prefabid") && block.getInt("prefabid") >= idctr) {
	            idctr = block.getInt("prefabid") + 1;
            }
        }
        for(String id : eManager.currentMap.scene.getThingMap("THING_COLLISION").keySet()) {
            gThing collision = eManager.currentMap.scene.getThingMap("THING_COLLISION").get(id);
            if(collision.contains("prefabid") && collision.getInt("prefabid") >= idctr) {
                idctr = collision.getInt("prefabid") + 1;
            }
        }
	    return idctr;
    }

	public gScene copy() {
	    gScene toReturn = new gScene();
	    toReturn.objectLists = new HashMap<>();
	    toReturn.objectMaps = new HashMap<>();
	    for(String s : object_titles) {
	        toReturn.objectLists.put(s, new ArrayList<>(objectLists.get(s)));
        }
	    //OBJECTMAP BELOW
        for(String objectType : objectMaps.keySet()) {
            toReturn.objectMaps.put(objectType, new HashMap<>(objectMaps.get(objectType)));
        }
	    return toReturn;
    }

    public void clearPlayers() {
	    objectMaps.put("THING_PLAYER", new HashMap<>());
	    cGameLogic.setUserPlayer(null);
    }

	public ArrayList<gTile> tiles() {
		return objectLists.get("THING_TILE");
	}

    public ArrayList<gFlare> flares() {
        return objectLists.get("THING_FLARE");
    }

    public ArrayList<gThing> getThingList(String thing_title) {
        return objectLists.get(thing_title);
    }

    public HashMap<String, gThing> getThingMap(String thing_title) {
	    return objectMaps.get(thing_title);
    }

    public void setThingMap(String thing_title, HashMap<String, String> nm) {
	    objectMaps.put(thing_title, nm);
    }

    public void setPlayersMap(HashMap<String, gPlayer> hm) {
	    objectMaps.put("THING_PLAYER", hm);
    }

    public HashMap<String, gPlayer> playersMap() {
	    return objectMaps.get("THING_PLAYER");
    }

    public static Collection<String> getPlayerIds() {
        return eManager.currentMap.scene.playersMap().keySet();
    }

    public static gPlayer getPlayerById(String id) {
        return eManager.currentMap.scene.playersMap().get(id);
    }
}