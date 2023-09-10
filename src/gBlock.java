import java.awt.*;

public class gBlock extends gThing {
    String prefabId;

    public gBlock(String id, String prefabId, int x, int y, int w, int h) {
        super();
        this.id = id;
        this.prefabId = prefabId;
        this.type = "THING_BLOCK";
        coords[0] = x;
        coords[1] = y;
        dims[0] = w;
        dims[1] = h;
    }

    public void draw(Graphics2D g2) {
        //to be overridden
    }

    public void drawPreview(Graphics2D g2) {
        //to be overridden
    }

    public void addToScene(gScene scene) {
        super.addToScene(scene);
        scene.getThingMap("THING_BLOCK").put(id, this);
    }
}