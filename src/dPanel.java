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
        else {
            g2.translate(sSettings.width / 2, sSettings.height / 2);
            g2.scale(eUtils.zoomLevel, eUtils.zoomLevel);
            g2.translate(-sSettings.width / 2, -sSettings.height / 2);
            if(cVars.isOne("maploaded")) {
                gScene scene = cClientLogic.scene;
                dBlockFloors.drawBlockFloors(g2, scene);
                dBlockWalls.drawBlockWalls(g2, scene);
                dItems.drawItems(g2, scene);
                dPlayer.drawPlayers(g2);
                dTileTops.drawTops(g2, scene);
                dFlares.drawSceneFlares(g2, scene);
                dTileTops.drawBullets(g2, scene);
                dAnimations.drawAnimations(g2, scene);
                dWaypoints.drawWaypoints(g2, scene);
                dTileTops.drawPopups(g2, scene);
                dTileTops.drawUserPlayerArrow(g2);
                dTileTops.drawPlayerNames(g2);
                if(!uiInterface.inplay && sSettings.show_mapmaker_ui)
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
