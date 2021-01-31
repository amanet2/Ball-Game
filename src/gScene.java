import java.util.ArrayList;
import java.util.Arrays;
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

	HashMap<String, ArrayList> objects;
	HashMap<String, HashMap> objectsMap;

	public gScene() {
		objects = new HashMap<>();
		objectsMap = new HashMap<>();
		for(String s : object_titles) {
			objects.put(s, new ArrayList<>());
		}
		for(String s : object_titles) {
		    objectsMap.put(s, new HashMap<>());
        }
	}

	public gScene copy() {
	    gScene toReturn = new gScene();
	    toReturn.objects = new HashMap<>();
	    toReturn.objectsMap = new HashMap<>();
	    for(String s : object_titles) {
	        toReturn.objects.put(s, new ArrayList<>(objects.get(s)));
        }
	    //OBJECTMAP BELOW
//        HashMap‘s parameterized constructor HashMap(Map<? extends K,? extends V> m)
//        provides a quick way to shallow copy an entire map:
//
//        HashMap<String, Employee> shallowCopy = new HashMap<String, Employee>(originalMap);
        for(String objectType : objectsMap.keySet()) {
            toReturn.objectsMap.put(objectType, new HashMap<>(objectsMap.get(objectType)));
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

	public ArrayList<gBullet> bullets() {
	    return objects.get("THING_BULLET");
    }

	public ArrayList<gTile> tiles() {
		return objects.get("THING_TILE");
	}

    public ArrayList<gPlayer> players() {
        return objects.get("THING_PLAYER");
    }

    public ArrayList<gPlayer> botplayers() {
        return objects.get("THING_BOTPLAYER");
    }

    public ArrayList<gPopup> popups() {
        return objects.get("THING_POPUP");
    }

    public ArrayList<gProp> props() {
        return objects.get("THING_PROP");
    }

    public HashMap<String, gThing> getThingMap(String thing_title) {
	    return objectsMap.get(thing_title);
    }

    public ArrayList<gPropScorepoint> scorepoints() {
        return objects.get("PROP_SCOREPOINT");
    }

    public ArrayList<gPropBallBouncy> ballbouncys() {
        return objects.get("PROP_BALLBOUNCY");
    }

    public ArrayList<gPropFlagRed> flagsred() {
        return objects.get("PROP_FLAGRED");
    }

    public ArrayList<gPropPowerup> powerups() {
	    return objects.get("PROP_POWERUP");
    }

    public ArrayList<gFlare> flares() {
        return objects.get("THING_FLARE");
    }

    public ArrayList<gAnimationEmitter> animations() {
        return objects.get("THING_ANIMATION");
    }

    public static String getObjTitleForCode(int code) {
	    return object_titles[code];
    }

    public static int getObjCodeForTitle(String title) {
	    int i = 0;
        for(String s : object_titles) {
            if(s.equals(title))
                return i;
            i++;
        }
        return i;
    }
}