import java.awt.*;

public class gBlock extends gThing {
    public gBlock(String id, String prefabId, String type, int x, int y, int w, int h) {
        super();
        this.id = id;
        this.prefabId = prefabId;
        this.type = type;
        coords[0] = x;
        coords[1] = y;
        dims[0] = w;
        dims[1] = h;
    }

    public void draw(Graphics2D g2) {
        //to be overridden
    }

    public void put(gScene scene) {
        super.put(scene);
        scene.getThingMap("THING_BLOCK").put(id, this);
    }
}
