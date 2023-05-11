import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

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
        drawFrame(g2v);
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
        gMessages.checkMessages();
    }

    public void drawFrame(Graphics2D g2) {
        if(!cClientLogic.maploaded) // comment out for no loading screens
            return;
        g2.translate(sSettings.width / 2, sSettings.height / 2);
        g2.scale(sSettings.zoomLevel, sSettings.zoomLevel);
        g2.translate(-sSettings.width / 2, -sSettings.height / 2);
        gScene scene = xMain.shellLogic.clientScene;
        g2.scale(
            ((1.0 / sSettings.gamescale) * (double) sSettings.height),
            ((1.0 / sSettings.gamescale) * (double) sSettings.height)
        );
        g2.translate(-gCamera.getX(), -gCamera.getY());
        dBlockFloors.drawBlockFloors(g2, scene);
        dBlockWalls.drawBlockWallsAndPlayers(g2, scene);
        dTileTops.drawMapmakerOverlay(g2, scene);
        dTileTops.drawBulletsAndAnimations(g2, scene);
        dWaypoints.drawWaypoints(g2, scene);
        dTileTops.drawPopups(g2, scene);
        dTileTops.drawUserPlayerArrow(g2);
        dTileTops.drawPlayerNames(g2);
        if(sSettings.show_mapmaker_ui)
            dMapmakerOverlay.drawSelectionBoxes(g2);
    }

    public dPanel() {
        super();
        super.setDoubleBuffered(false);
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(false);
    }
}
