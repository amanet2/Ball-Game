import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class vFrameFactory {

    public static void drawFrame(Graphics2D g2, int panelLevel) {
        if (panelLevel == 1) {
            dScreenFX.drawScreenFX(g2);
            dScreenMessages.displayScreenMessages(g2);
        } else {
            g2.translate(sSettings.width / 2, sSettings.height / 2);
            g2.scale(eUtils.zoomLevel, eUtils.zoomLevel);
            g2.translate(-sSettings.width / 2, -sSettings.height / 2);
            dTileFloors.drawFloors(g2);
            dTileWalls.drawWalls(g2);
            dProp.drawProps(g2);
            dPlayer.drawPlayers(g2);
            dTileTops.drawTops(g2);
        }
        g2.dispose();
    }
}
