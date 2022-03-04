import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dBlockFloors {
    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        gBlockFactory.instance().floorTexture = new TexturePaint(gBlockFactory.instance().floorImage,
                new Rectangle2D.Double(
                        eUtils.scaleInt(-cVars.getInt("camx")),
                        eUtils.scaleInt(-cVars.getInt("camy")),
                        eUtils.scaleInt(1210),
                        eUtils.scaleInt(1210)));
        for(String tag : floorMap.keySet()) {
            gBlockFloor block = (gBlockFloor) floorMap.get(tag);
            drawBlockFloor(g2, block);
        }
//        //shading
//        for(String tag : floorMap.keySet()) {
//            gBlockFloor block = (gBlockFloor) floorMap.get(tag);
//            dBlockFloorsShading.drawBlockFloorShading(g2, block);
//        }
    }

    public static void drawBlockFloor(Graphics2D g2, gBlockFloor block) {
//        g2.setColor(Color.GRAY);
//        gBlockFactory.instance().floorTexture = new TexturePaint(gBlockFactory.instance().floorImage,
//                new Rectangle2D.Double(
//                        eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")),
//                        eUtils.scaleInt(1210),
//                        eUtils.scaleInt(1210)));
        g2.setPaint(gBlockFactory.instance().floorTexture);
        g2.fillRect(
                eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")) - 5,
                eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")) - 5,
                eUtils.scaleInt(block.getInt("dimw")) + 10,
                eUtils.scaleInt(block.getInt("dimh")) + 10
        );
//        dBlockFloorsShading.drawBlockFloorShading(g2, block);
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
//        dBlockFloorsShading.drawBlockFloorShading(g2, block);
    }
}
