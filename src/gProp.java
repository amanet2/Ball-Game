import java.awt.*;
import java.awt.geom.Ellipse2D;

public class gProp extends gThing {
    Image sprite;

    public gProp load(String[] args) {
        return null;
    }

    public void propEffect(gPlayer p) {
        xCon.ex("echo Unspecified prop effect for prop tag/code: "+get("tag")+"/"+get("code"));
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
        put("sprite", eUtils.getPath(gProps.spritesSelection[getInt("code")]));
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
