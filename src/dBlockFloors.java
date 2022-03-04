import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dBlockFloors {
    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        gBlockFactory.instance().floorTexture = new TexturePaint(gBlockFactory.instance().floorImage,
                new Rectangle2D.Double(
                        0,
                        0,
                        1210,
                        1210));
        for(String tag : floorMap.keySet()) {
            gBlockFloor block = (gBlockFloor) floorMap.get(tag);
            drawBlockFloor(g2, block);
        }
    }

    public static void drawBlockFloor(Graphics2D g2, gBlockFloor block) {
        g2.setPaint(gBlockFactory.instance().floorTexture);
        g2.fillRect(
                block.getInt("coordx") - 5,
                block.getInt("coordy") - 5,
                block.getInt("dimw") + 10,
                block.getInt("dimh") + 10
        );
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gBlockFloor block = (gBlockFloor) floorMap.get(tag);
            drawMapmakerPreviewBlockFloor(g2, block);
        }
    }

    public static void drawMapmakerPreviewBlockFloor(Graphics2D g2, gBlockFloor block) {
        g2.setColor(gBlockFactory.instance().floorColorPreview);
        g2.fillRect(
                eUtils.scaleInt(block.getInt("coordx")/4),
                eUtils.scaleInt(block.getInt("coordy")/4),
                eUtils.scaleInt((block.getInt("dimw"))/4),
                eUtils.scaleInt((block.getInt("dimh"))/4)
        );
    }
}
