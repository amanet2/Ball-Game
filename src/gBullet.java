import java.awt.*;
import java.awt.geom.Ellipse2D;

public class gBullet extends gThing {
    Image sprite;

    public boolean doesCollideWithPlayer(gPlayer p) {
        Ellipse2D bounds = new Ellipse2D.Double(getInt("coordx"), getInt("coordy"),
                getInt("dimw"), getInt("dimh"));
        return bounds.intersects(new Rectangle(p.getInt("coordx"), p.getInt("coordy"),
                p.getInt("dimw"), p.getInt("dimh")));
    }


    public gBullet(int x, int y, int w, int h, String tt, double fv, int dmg) {
        super();
        put("sprite", tt);
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("dmg", dmg);
        putInt("src", gWeapons.type.NONE.code());
        putInt("anim", -1);
        putInt("ttl", 1000);
        putLong("timestamp", System.currentTimeMillis());
        putDouble("fv", fv);
        put("id", cScripts.createId());
        put("srcid", "God");
        putInt("isexplosionpart", 0);
        sprite = gTextures.getScaledImage(tt, getInt("dimw"), getInt("dimh"));
    }
}
