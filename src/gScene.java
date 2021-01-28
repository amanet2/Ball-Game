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

    static final String[] object_titles = new String[]{
        "THING_TILE","THING_PLAYER","THING_BULLET","THING_POPUP","THING_PROP","THING_FLARE","THING_ANIMATION",
            "THING_BOTPLAYER", "PROP_TELEPORTER", "PROP_SCOREPOINT"
    };
	HashMap<String, ArrayList> objects;

	public gScene() {
		objects = new HashMap<>();
		for(String s : object_titles) {
			objects.put(s, new ArrayList<>());
		}
	}

	public gScene copy() {
	    gScene toReturn = new gScene();
	    toReturn.objects = new HashMap<>();
	    for(String s : object_titles) {
	        toReturn.objects.put(s, new ArrayList<>(objects.get(s)));
        }
	    return toReturn;
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

    public ArrayList<gPropTeleporter> teleporters() {
	    return objects.get("PROP_TELEPORTER");
    }

    public ArrayList<gPropScorepoint> scorepoints() {
        return objects.get("PROP_SCOREPOINT");
    }

    public ArrayList<gProp> scorePointsOld() {
	    ArrayList<gProp> scorepoints = new ArrayList<>();
	    for(gProp prop : props()) {
	        if(prop.isInt("code", gProp.SCOREPOINT))
	            scorepoints.add(prop);
        }
	    return scorepoints;
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