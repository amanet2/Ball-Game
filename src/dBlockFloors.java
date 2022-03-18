import java.awt.*;
import java.util.HashMap;

public class dBlockFloors {
    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gBlockFloor block = (gBlockFloor) floorMap.get(tag);
            drawBlockFloor(g2, block);
        }
    }

    public static void drawBlockFloor(Graphics2D g2, gBlockFloor block) {
        g2.setPaint(gBlockFactory.instance().floorTexture);
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getHeight());
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
        g2.fillRect(eUtils.scaleInt(block.getX()/4),
                    eUtils.scaleInt(block.getY()/4),
                    eUtils.scaleInt(block.getWidth()/4),
                    eUtils.scaleInt(block.getHeight()/4)
        );
    }
}
