import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Queue;

public class dBlockWalls {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.isVal("type", "THING_PLAYER")) {
                gPlayer player = (gPlayer) thing;
                dPlayer.drawPlayer(g2, player);
            }
            else if(thing.get("type").contains("ITEM_")) {
                gItem item = (gItem) thing;
                dItems.drawItem(g2, item);
            }
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
//            g2.setColor(Color.GRAY ));
            gBlockFactory.instance().wallTexture = new TexturePaint(gBlockFactory.instance().wallImage,
                    new Rectangle2D.Double(
                            eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")),
                            eUtils.scaleInt(300),
                            eUtils.scaleInt(300)));
            g2.setPaint(gBlockFactory.instance().wallTexture);
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("wallh"))
            );
            dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
        }
    }

    public static void drawBlockWallCubePreview(Graphics2D g2, gBlockCube block) {
        if (block.contains("wallh")) {
            g2.setColor(gBlockFactory.instance().wallColorPreview);
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx")/4),
                    eUtils.scaleInt(block.getInt("coordy")/4+ block.getInt("toph")/4),
                    eUtils.scaleInt(block.getInt("dimw")/4),
                    eUtils.scaleInt(block.getInt("wallh")/4)
            );
//            dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
        }
    }
}
