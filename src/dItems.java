import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class dItems {
    public static void drawItems(Graphics2D g2) {
        if(cVars.isOne("maploaded")) {
            for(String itemId : eManager.currentMap.scene.getThingMap("THING_ITEM").keySet()) {
                gItem item = (gItem) eManager.currentMap.scene.getThingMap("THING_ITEM").get(itemId);
                if(item.sprite != null) {

                }
                else {
                    g2.setColor(new Color(255, 150, 80, 150));
                    g2.fillRect(eUtils.scaleInt(item.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(item.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(item.getInt("dimw")),
                            eUtils.scaleInt(item.getInt("dimh")));
                }
            }
        }
    }
}
