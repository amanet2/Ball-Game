import javax.swing.*;
import java.awt.*;

/**
 * JPanel
 * displays image on the screen
 */
public class dPanel extends JPanel {
    int panelLevel = 0;
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        removeAll();
        //drawpanel
        Graphics2D g2 = (Graphics2D) g;
        drawFrame(g2, panelLevel);
        g2.dispose();
        g.dispose();
    }

    public void drawFrame(Graphics2D g2, int panelLevel) {
        if (panelLevel == 1) {
            dScreenFX.drawScreenFX(g2);
            dScreenMessages.displayScreenMessages(g2);
        }
        else if(sSettings.IS_CLIENT) {
            g2.translate(sSettings.width / 2, sSettings.height / 2);
            g2.scale(eUtils.zoomLevel, eUtils.zoomLevel);
            g2.translate(-sSettings.width / 2, -sSettings.height / 2);
            if(cVars.isOne("maploaded")) {
                dBlockFloors.drawBlockFloors(g2);
                dBlockWalls.drawBlockWalls(g2);
                dItems.drawItems(g2);
                dPlayer.drawPlayers(g2);
                dTileTops.drawTops(g2);
            }
            //mapmaker indicators
            if(sSettings.show_mapmaker_ui) {
                dMapmakerOverlay.drawSelectionBoxes(g2);
            }
        }
        g2.dispose();
    }

    public dPanel() {
        super();
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(true);
    }
}
