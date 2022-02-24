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
//        String[] colorvals = block.get("color").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        g2.fillRect(
//                eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")) - 5,
//                eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")) - 5,
//                eUtils.scaleInt(block.getInt("dimw")) + 10,
//                eUtils.scaleInt(block.getInt("dimh")) + 10
//        );
        g2.drawImage(gBlockFactory.instance().floorSprite,
                eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")) - 5,
                eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")) - 5, null);
        dBlockFloorsShading.drawBlockFloorShading(g2, block);
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gBlockFloor block = (gBlockFloor) floorMap.get(tag);
            drawMapmakerPreviewBlockFloor(g2, block);
        }
    }

    public static void drawMapmakerPreviewBlockFloor(Graphics2D g2, gBlockFloor block) {
        String[] colorvals = block.get("color").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        g2.fillRect(
                eUtils.scaleInt(block.getInt("coordx")/4),
                eUtils.scaleInt(block.getInt("coordy")/4),
                eUtils.scaleInt((block.getInt("dimw"))/4),
                eUtils.scaleInt((block.getInt("dimh"))/4)
        );
//        dBlockFloorsShading.drawBlockFloorShading(g2, block);
    }
}
