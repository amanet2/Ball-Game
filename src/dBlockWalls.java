import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Queue;

public class dBlockWalls {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.isVal("type", "THING_PLAYER"))
                dPlayer.drawPlayer(g2, (gPlayer) thing);
            else if(thing.get("type").contains("ITEM_"))
                dItems.drawItem(g2, (gItem) thing);
            else {
                if(thing.get("type").contains("CUBE")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockFlat(g2, (gBlockCube) thing);
                        drawBlockWallCube(g2, (gBlockCube) thing);
                        dBlockTops.drawBlockTopCube(g2, (gBlockCube) thing);
                    }
                }
            }
        }
    }

    public static void drawBlockWallCube(Graphics2D g2, gBlockCube block) {
        if (block.contains("wallh")) {
            gBlockFactory.instance().wallTexture = new TexturePaint(gBlockFactory.instance().wallImage,
                    new Rectangle2D.Double(block.getX(), block.getY(), 300, 300));
            g2.setPaint(gBlockFactory.instance().wallTexture);
            g2.fillRect(
                    block.getX(), block.getY() + block.getInt("toph"),
                    block.getWidth(), block.getInt("wallh")
            );
            dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
        }
    }

    public static void drawBlockWallCubePreview(Graphics2D g2, gBlockCube block) {
        if (block.contains("wallh")) {
            g2.setColor(gBlockFactory.instance().wallColorPreview);
            g2.fillRect(
                    eUtils.scaleInt(block.getX()/4),
                    eUtils.scaleInt(block.getY()/4+ block.getInt("toph")/4),
                    eUtils.scaleInt(block.getWidth()/4),
                    eUtils.scaleInt(block.getInt("wallh")/4)
            );
        }
    }
}
