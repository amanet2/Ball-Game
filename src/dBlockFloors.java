import java.awt.Graphics2D;
import java.util.HashMap;

public class dBlockFloors {
    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            g2.setPaint(xMain.shellLogic.blockFactory.floorTexture);
            g2.fillRect(block.getInt("coordx"), block.getY(), block.getWidth(), block.getHeight());
        }
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            dFonts.setFontColor(g2, "clrw_floorcolorpreview");
            g2.fillRect(eUtils.scaleInt(block.getX()/4),
                    eUtils.scaleInt(block.getY()/4),
                    eUtils.scaleInt(block.getWidth()/4),
                    eUtils.scaleInt(block.getHeight()/4)
            );
        }
    }
}
