import java.awt.*;

public class gBlockFloor extends gBlock {
    TexturePaint texture;

    public gBlockFloor(String id, String prefabId, int x, int y) {
        super(id, prefabId, "BLOCK_FLOOR", x, y, 1200, 1200);
        texture = xMain.shellLogic.floorTexture;
    }

    public void draw(Graphics2D g2) {
        g2.setPaint(texture);
        g2.fillRect(coords[0], coords[1], dims[0], dims[1]);
    }
}
