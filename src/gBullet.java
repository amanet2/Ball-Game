import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class gBullet extends gThing {
    Image sprite;

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
        putLong("timestamp", gTime.gameTime);
        putDouble("fv", fv);
        put("id", eManager.createId());
        put("srcid", "God");
        putInt("isexplosionpart", 0);
        sprite = gTextures.getGScaledImage(tt, getInt("dimw"), getInt("dimh"));
    }
}
