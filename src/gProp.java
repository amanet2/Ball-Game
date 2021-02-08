import java.awt.*;
import java.awt.geom.Ellipse2D;

public class gProp extends gThing {
    Image sprite;

    static final int TELEPORTER = 0;
    static final int SCOREPOINT = 1;
    static final int FLAGRED = 2;
    static final int FLAGBLUE = 3;
    static final int POWERUP = 4;
    static final int BOOSTUP = 5;
    static final int SPAWNPOINT = 6;

    public boolean isType(int code) {
        return isInt("code", code);
    }

    public static String getSaveStringForCode(int code) {
        return saveStrings[code];
    }

    public static String getSaveStringForProp(gProp prop) {
        return getSaveStringForCode(prop.getInt("code"));
    }

    //old and shitty
    static String[] propSelection = new String[]{
            "Teleporter",
            "Score Point",
            "Flag-Red",
            "Flag-Blue",
            "Powerup",
            "Boostup",
            "Spawn Point"
    };

    static String[] propSpriteSelection = new String[]{
            "misc/misc_energy_ball_trans_purple.png",
            "none",
            "misc/flag_red.png",
            "misc/flag_blue.png",
            "misc/powerup.png",
            "misc/boostup.png",
            "none"
    };

    static String[] saveStrings = new String[]{
            "PROP_TELEPORTER",
            "PROP_SCOREPOINT",
            "PROP_FLAGRED",
            "PROP_FLAGBLUE",
            "PROP_POWERUP",
            "PROP_BOOSTUP",
            "PROP_SPAWNPOINT"
    };

    public gProp load(String[] args) {
        return null;
    }

    public void propEffect(gPlayer p) {
        xCon.ex("echo Unspecified prop effect for prop tag/code: "+get("tag")+"/"+get("code"));
    }

    static int getCodeForTitle(String title) {
        for(int i = 0; i < propSelection.length;i++) {
            if(title.equalsIgnoreCase(propSelection[i]))
                return i;
        }
        return -1;
    }

    public void setSpriteFromPath(String newpath) {
        put("sprite", newpath);
        sprite = gTextures.getScaledImage(get("sprite"), getInt("dimw"), getInt("dimh"));
    }

    public gProp(int t, int ux, int uy, int x, int y, int w, int h) {
        super();
        put("type", "THING_PROP");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("code", t);
        put("sprite", eUtils.getPath(propSpriteSelection[getInt("code")]));
        put("fv", "0.0");
        putInt("int0", ux);
        putInt("int1", uy);
        put("str0","null");
        putInt("botint0", ux);
        put("racebotidcheckins", "");
        put("accelrate", "100");
        put("clip", "1");
        put("crouch", "0");
        put("acceltick", "0");
        put("vel0", "0");
        put("vel1", "0");
        put("vel2", "0");
        put("vel3", "0");
        put("mov0", "0");
        put("mov1", "0");
        put("mov2", "0");
        put("mov3", "0");
        put("native","0");
        put("id", cScripts.createID(8));
        sprite = gTextures.getScaledImage(get("sprite"), getInt("dimw"), getInt("dimh"));
    }
}
