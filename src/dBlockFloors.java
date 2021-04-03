import java.awt.*;
import java.util.HashMap;

public class dBlockFloors {
    public static void drawBlockFloors(Graphics2D g2) {
        HashMap<String, gThing> squareMap = eManager.currentMap.scene.getThingMap("BLOCK_FLOOR");
        for(String tag : squareMap.keySet()) {
            gBlockFloor block = (gBlockFloor) squareMap.get(tag);
            String[] colorvals = block.get("color").split("\\.");
            g2.setColor(new Color(
                    Integer.parseInt(colorvals[0]),
                    Integer.parseInt(colorvals[1]),
                    Integer.parseInt(colorvals[2]),
                    Integer.parseInt(colorvals[3])
            ));
            g2.fillRect(
                    eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("dimh"))
            );
//            g2.fillRect(
//                    eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")) - 5,
//                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")) - 5,
//                    eUtils.scaleInt(block.getInt("dimw")) + 10,
//                    eUtils.scaleInt(block.getInt("dimh")) + 10
//            );
            dBlockFloorsShading.drawBlockFloorShading(g2, block);
        }
    }
}
