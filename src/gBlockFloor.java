import java.awt.*;

public class gBlockFloor extends gThing {
    public gBlockFloor(int x, int y, int w, int h) {
        super();
        coords = new int[]{x, y};
        dims = new int[]{w, h};
    }

    public void draw(Graphics2D g2) {
        g2.setPaint(xMain.shellLogic.floorTexture);
        g2.fillRect(coords[0], coords[1], dims[0], dims[1]);
    }
}
