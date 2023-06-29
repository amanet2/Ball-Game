import java.awt.*;

public class gBlockCollision extends gBlock {
    public gBlockCollision(String id, String prefabId, int x, int y, int w, int h) {
        super(id, prefabId, x, y, w, h);
        this.type = "BLOCK_COLLISION";
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawRect(coords[0], coords[1], dims[0], dims[1]);
    }
}