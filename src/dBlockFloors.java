import java.awt.*;
import java.util.HashMap;

public class dBlockFloors {
    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            drawBlockFloor(g2, block);
        }
    }

    public static void drawBlockFloor(Graphics2D g2, gThing block) {
        g2.setPaint(gBlockFactory.instance().floorTexture);
        g2.fillRect(block.getInt("coordx"), block.getY(), block.getWidth(), block.getHeight());
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            drawMapmakerPreviewBlockFloor(g2, block);
        }
    }

    public static void drawMapmakerPreviewBlockFloor(Graphics2D g2, gThing block) {
        dFonts.setFontColor(g2, "clrw_floorcolorpreview");
        g2.fillRect(eUtils.scaleInt(block.getX()/4),
                    eUtils.scaleInt(block.getY()/4),
                    eUtils.scaleInt(block.getWidth()/4),
                    eUtils.scaleInt(block.getHeight()/4)
        );
    }
}
