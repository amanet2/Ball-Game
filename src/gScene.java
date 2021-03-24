import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    static final int THING_TILE = 0;
    static final int THING_PROP = 4;
    static final int THING_FLARE = 5;

    static final String[] object_titles = new String[]{
        "THING_TILE","THING_PLAYER","THING_BULLET","THING_POPUP","THING_PROP","THING_FLARE","THING_ANIMATION",
            "THING_BOTPLAYER", "PROP_TELEPORTER", "PROP_SCOREPOINT", "PROP_BOOST", "PROP_FLAGBLUE",
            "PROP_FLAGRED", "PROP_POWERUP", "PROP_SPAWNPOINT", "THING_BLOCK", "BLOCK_CUBE", "BLOCK_FLOOR",
            "BLOCK_CORNERUR", "BLOCK_CORNERLR", "BLOCK_CORNERLL", "BLOCK_CORNERUL", "THING_PREFAB", "PREFAB_CAGE",
            "PREFAB_CAGEB", "PREFAB_COLUMN", "PREFAB_ARCHWAY", "PREFAB_BIGBOX"
    };

	HashMap<String, ArrayList> objectLists;
	HashMap<String, HashMap> objectMaps;
    HashMap<String, Integer> objectIdCtrs;

	public gScene() {
		objectLists = new HashMap<>();
		objectMaps = new HashMap<>();
		objectIdCtrs = new HashMap<>();
		for(String s : object_titles) {
			objectLists.put(s, new ArrayList<>());
            objectMaps.put(s, new HashMap<>());
            objectIdCtrs.put(s, 0);
        }
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
    }

	public ArrayList<gTile> tiles() {
		return objectLists.get("THING_TILE");
	}

    public ArrayList<gProp> props() {
        return objectLists.get("THING_PROP");
    }

//    public ArrayList<gBlock> blocks() {
//        return objectLists.get("THING_BLOCK");
//    }

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

    static String getObjTitleForCode(int code) {
	    return object_titles[code];
    }

    static int getObjCodeForTitle(String title) {
	    int i = 0;
        for(String s : object_titles) {
            if(s.equals(title))
                return i;
            i++;
        }
        return i;
    }
}