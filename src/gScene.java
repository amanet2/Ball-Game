import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

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
            "THING_BOTPLAYER", "PROP_TELEPORTER", "PROP_SCOREPOINT", "PROP_BOOSTUP", "PROP_FLAGBLUE",
            "PROP_FLAGRED", "PROP_POWERUP", "PROP_SPAWNPOINT"
    };

	HashMap<String, ArrayList> objectLists;
	HashMap<String, HashMap> objectMaps;

	public gScene() {
		objectLists = new HashMap<>();
		objectMaps = new HashMap<>();
		for(String s : object_titles) {
			objectLists.put(s, new ArrayList<>());
            objectMaps.put(s, new HashMap<>());
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

	public ArrayList<gTile> tiles() {
		return objectLists.get("THING_TILE");
	}

    public ArrayList<gPlayer> players() {
        return objectLists.get("THING_PLAYER");
    }

    public ArrayList<gProp> props() {
        return objectLists.get("THING_PROP");
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