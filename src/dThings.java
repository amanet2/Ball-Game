import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class dThings {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.type.equals("THING_PLAYER"))
                ((gPlayer) thing).draw(g2);
            else if(thing.type.contains("ITEM_"))
                ((gItem) thing).draw(g2);
            else if(thing.type.contains("CUBE")) {
                ((gBlockCube) thing).draw(g2);
            }
        }
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing floor = floorMap.get(tag);
            //flashlight
            if(xMain.shellLogic.getUserPlayer() != null) {
                int aimerx = eUtils.unscaleInt(uiInterface.getMouseCoordinates()[0]);
                int aimery = eUtils.unscaleInt(uiInterface.getMouseCoordinates()[1]);
                int snapX = aimerx + gCamera.coords[0];
                int snapY = aimery + gCamera.coords[1];
                int setw = sSettings.height;
                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY), setw/2,
                        new float[]{0f, 1f}, new Color[]{new Color(0,0,0,0), gColors.getColorFromName("clrw_floorshading")}
                );
                g2.setPaint(df);
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
            else {
                g2.setColor(gColors.getColorFromName("clrw_floorshading"));
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
        }
    }

    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            ((gBlockFloor) floorMap.get(tag)).draw(g2);
        }
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            ((gBlockFloor) floorMap.get(tag)).drawPreview(g2);
        }
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        ConcurrentHashMap<String, gThing> squareMap = xMain.shellLogic.clientPreviewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            ((gBlockCube) squareMap.get(tag)).drawPreview(g2);
        }
    }
}
