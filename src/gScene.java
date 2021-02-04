import java.util.ArrayList;
import java.util.HashMap;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    static final int THING_TILE = 0;
    static final int THING_PLAYER = 1;
    static final int THING_BULLET = 2;
    static final int THING_POPUP = 3;
    static final int THING_PROP = 4;
    static final int THING_FLARE = 5;
    static final int THING_ANIMATION = 6;
    static final int THING_BOTPLAYER = 7;
    static final int PROP_TELEPORTER = 8;
    static final int PROP_SCOREPOINT = 9;
    static final int PROP_BOOSTUP = 10;
    static final int PROP_BALLBOUNCY = 11;
    static final int PROP_FLAGBLUE = 12;
    static final int PROP_FLAGRED = 13;
    static final int PROP_POWERUP = 14;

    static final String[] object_titles = new String[]{
        "THING_TILE","THING_PLAYER","THING_BULLET","THING_POPUP","THING_PROP","THING_FLARE","THING_ANIMATION",
            "THING_BOTPLAYER", "PROP_TELEPORTER", "PROP_SCOREPOINT", "PROP_BOOSTUP", "PROP_BALLBOUNCY",
            "PROP_FLAGBLUE", "PROP_FLAGRED", "PROP_POWERUP"
    };

	HashMap<String, ArrayList> objectLists;
	HashMap<String, HashMap> objectMaps;

	public gScene() {
		objectLists = new HashMap<>();
		objectMaps = new HashMap<>();
		for(String s : object_titles) {
			objectLists.put(s, new ArrayList<>());
		}
		for(String s : object_titles) {
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

    public String getPropArrayTitleForProp(gProp prop) {
        switch (prop.getInt("code")) {
            case gProp.TELEPORTER:
                return "PROP_TELEPORTER";
            case gProp.SCOREPOINT:
                return "PROP_SCOREPOINT";
            case gProp.FLAGRED:
                return "PROP_FLAGRED";
            case gProp.FLAGBLUE:
                return "PROP_FLAGBLUE";
            case gProp.POWERUP:
                return "PROP_POWERUP";
            case gProp.BOOSTUP:
                return "PROP_BOOSTUP";
            case gProp.BALLBOUNCY:
                return "PROP_BALLBOUNCY";
            default:
                return null;
        }
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

//    public ArrayList<gFlare> flares() {
//        return objects.get("THING_FLARE");
//    }

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