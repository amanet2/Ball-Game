import javax.swing.*;
import java.awt.*;

/**
 * JPanel
 * displays image on the screen
 */
public class dPanel extends JPanel {
    public void paintComponent(Graphics g){
        removeAll();
        Graphics2D g2v = (Graphics2D) g.create();
        Graphics2D g2u = (Graphics2D) g.create();
        long gameTime = gTime.gameTime;
        drawFrame(g2v, gameTime);
        drawFrameUI(g2u, gameTime);
        uiInterface.frames++;
        g2v.dispose();
        g2u.dispose();
        g.dispose();
    }

    public void drawFrameUI(Graphics2D g2, long gameTimeMillis) {
        dScreenFX.drawScreenFX(g2);
        dScreenMessages.displayScreenMessages(g2, gameTimeMillis);
        if(!uiInterface.inplay && sSettings.show_mapmaker_ui && cClientLogic.maploaded) {
            dBlockFloors.drawMapmakerPreviewBlockFloors(g2, uiEditorMenus.previewScene);
            dBlockTops.drawBlockTopCubesPreview(g2);
        }
    }

    public void drawFrame(Graphics2D g2, long gameTimeMillis) {
        if(cClientLogic.maploaded) {
            g2.translate(sSettings.width / 2, sSettings.height / 2);
            g2.scale(eUtils.zoomLevel, eUtils.zoomLevel);
            g2.translate(-sSettings.width / 2, -sSettings.height / 2);
            gScene scene = cClientLogic.scene;
            g2.scale(
                ((1.0 / sSettings.gamescale) * (double) sSettings.height),
                ((1.0 / sSettings.gamescale) * (double) sSettings.height)
            );
            g2.translate(-gCamera.getX(), -gCamera.getY());
            dBlockFloors.drawBlockFloors(g2, scene);
            dBlockWalls.drawBlockWallsAndPlayers(g2, scene);
            dTileTops.drawMapmakerOverlay(g2, scene);
            dTileTops.drawBullets(g2, scene);
            dAnimations.drawAnimations(g2, scene, gameTimeMillis);
            dWaypoints.drawWaypoints(g2, scene);
            dTileTops.drawPopups(g2, scene);
            dTileTops.drawUserPlayerArrow(g2);
            dTileTops.drawPlayerNames(g2);
            if(!uiInterface.inplay && sSettings.show_mapmaker_ui)
                dMapmakerOverlay.drawSelectionBoxes(g2);
        }
    }

    public dPanel() {
        super();
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(false);
    }
}
