import java.awt.*;
import java.awt.geom.Ellipse2D;

public class gBullet extends gThing {
    Image sprite;

    public boolean doesCollideWithPlayer(gPlayer p) {
        Ellipse2D bounds = new Ellipse2D.Double(getInt("coordx"), getInt("coordy"), getInt("dimw"), getInt("dimh"));
        return bounds.intersects(new Rectangle(p.getInt("coordx"), p.getInt("coordy"), p.getInt("dimw"), p.getInt("dimh")))
                && getInt("tag") != p.getInt("tag");
    }

    public boolean doesCollideWithinTile(gTile target) {
        Ellipse2D bounds = new Ellipse2D.Double(getInt("coordx"), getInt("coordy"), getInt("dimw"), getInt("dimh"));
        return bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")+75,
            target.getInt("dim0w"), target.getInt("dim0h")-75))
            || bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")
            + target.getInt("dim0h"), target.getInt("dim1w"), target.getInt("dim1h")-100))
            || bounds.intersects(new Rectangle(target.getInt("coordx"),
            target.getInt("coordy") + 75 + target.getInt("dimh") - target.getInt("dim3h")-target.getInt("dim4h"),
            target.getInt("dim3w"), target.getInt("dim3h")-75))
            || bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy") + target.getInt("dimh")
            - target.getInt("dim4h"), target.getInt("dim4w"), target.getInt("dim4h")-100))
            || bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")
            + target.getInt("dim0h"), target.getInt("dim5w"), target.getInt("dim5h")))
            || bounds.intersects(new Rectangle(target.getInt("coordx")+target.getInt("dimw")-target.getInt("dim6w"),
            target.getInt("coordy")+target.getInt("dim0h"), target.getInt("dim6w"),
            target.getInt("dim6h")));
    }

    public gBullet(int x, int y, int w, int h, String tt, double fv, int dmg) {
        super();
        put("sprite", tt);
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("dmg", dmg);
        putInt("src", gWeapons.weapon_none);
        putInt("anim", -1);
        putInt("ttl", 1000);
        putLong("timestamp", System.currentTimeMillis());
        putDouble("fv", fv);
        putInt("tag", -1);
        putInt("isexplosionpart", 0);
        sprite = gTextures.getScaledImage(tt, getInt("dimw"), getInt("dimh"));
    }
}
