import javax.swing.JPanel;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JPanel
 * displays image on the screen
 */
public class dPanel extends JPanel {
    public void paintComponent(Graphics g){
        removeAll();
        Graphics2D g2v = (Graphics2D) g.create();
        Graphics2D g2u = (Graphics2D) g.create();
        long gameTime = sSettings.gameTime;
        drawFrame(g2v);
        drawFrameUI(g2u, gameTime);
        sSettings.frames++;
        g2v.dispose();
        g2u.dispose();
        g.dispose();
    }

    private void drawFrameUI(Graphics2D g2, long gameTimeMillis) {
        dScreenFX.drawScreenFX(g2);
        dScreenMessages.displayScreenMessages(g2, gameTimeMillis);
        if(!sSettings.inplay && sSettings.show_mapmaker_ui && sSettings.clientMapLoaded) {
            drawMapmakerPreviewBlockFloors(g2, xMain.shellLogic.clientPreviewScene);
            drawBlockTopCubesPreview(g2);
        }
    }

    private void drawFrame(Graphics2D g2) {
        if(!sSettings.clientMapLoaded) // comment out for no loading screens
            return;
        g2.translate(sSettings.width / 2, sSettings.height / 2);
        g2.scale(sSettings.zoomLevel, sSettings.zoomLevel);
        g2.translate(-sSettings.width / 2, -sSettings.height / 2);
        gScene scene = xMain.shellLogic.clientScene;
        g2.scale(
            ((1.0 / sSettings.gamescale) * (double) sSettings.height),
            ((1.0 / sSettings.gamescale) * (double) sSettings.height)
        );
        g2.translate(-gCamera.coords[0], -gCamera.coords[1]);
        synchronized (scene.objectMaps) {
            drawBlockFloors(g2, scene);
            drawBlockWallsAndPlayers(g2, scene);
            dHUD.drawMapmakerOverlay(g2, scene);
            dHUD.drawBulletsAndAnimations(g2, scene);
            dHUD.drawWaypoints(g2, scene);
            dHUD.drawPopups(g2, scene);
            dHUD.drawUserPlayerArrow(g2);
            dHUD.drawPlayerNames(g2);
            if (sSettings.show_mapmaker_ui)
                dHUD.drawSelectionBoxes(g2);
        }
    }

    private void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.type.equals("THING_PLAYER"))
                ((gPlayer) thing).draw(g2);
            else if(thing.type.startsWith("ITEM_"))
                ((gItem) thing).draw(g2);
            else if(thing.type.contains("CUBE"))
                ((gBlockCube) thing).draw(g2);
        }
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing floor = floorMap.get(tag);
            //flashlight
            if(xMain.shellLogic.getUserPlayer() != null) {
                int aimerx = eUtils.unscaleInt(xMain.shellLogic.getMouseCoordinates()[0]);
                int aimery = eUtils.unscaleInt(xMain.shellLogic.getMouseCoordinates()[1]);
                int snapX = aimerx + (int) gCamera.coords[0];
                int snapY = aimery + (int) gCamera.coords[1];
                int setw = sSettings.height;
                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY), setw/2,
                        new float[]{0f, 1f}, new Color[]{new Color(0,0,0,0), gColors.getColorFromName("clrw_ambientshading")}
                );
                g2.setPaint(df);
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
            else {
                g2.setColor(gColors.getColorFromName("clrw_ambientshading"));
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
        }
    }

    private void drawBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            ((gBlockFloor) floorMap.get(tag)).draw(g2);
        }
    }

    private void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            ((gBlockFloor) floorMap.get(tag)).drawPreview(g2);
        }
    }

    private void drawBlockTopCubesPreview(Graphics2D g2) {
        ConcurrentHashMap<String, gThing> squareMap = xMain.shellLogic.clientPreviewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            ((gBlockCube) squareMap.get(tag)).drawPreview(g2);
        }
    }

    public dPanel() {
        super();
        super.setDoubleBuffered(false);
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(false);
    }
}
