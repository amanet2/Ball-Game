import java.awt.*;

public class gCollision extends gThing {
    Polygon collisionArea;
    public gCollision(int x, int y) {
        super();
        put("type", "THING_COLLISION");
        putInt("coordx", x);
        putInt("coordy", y);
    }
}
