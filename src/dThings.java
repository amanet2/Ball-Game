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
