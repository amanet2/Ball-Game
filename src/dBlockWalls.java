import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;

public class dBlockWalls {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        LinkedHashMap<String, gThing> combinedMap = scene.getWallsAndPlayersSortedByCoordY();
//        gBlockFactory.instance().wallTexture = new TexturePaint(gBlockFactory.instance().wallImage,
//                new Rectangle2D.Double(
//                        eUtils.scaleInt(-cVars.getInt("camx")),
//                        eUtils.scaleInt(-cVars.getInt("camy")),
//                        eUtils.scaleInt(300),
//                        eUtils.scaleInt(300)));
        for(String tag : combinedMap.keySet()) {
            gThing thing = combinedMap.get(tag);
            if(thing.contains("fv")) {
                gPlayer player = (gPlayer) thing;
                dPlayer.drawPlayer(g2, player);
            }
            else {
                if(thing.get("type").contains("CUBE")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockFlat(g2, (gBlockCube) thing);
                        drawBlockWallCube(g2, (gBlockCube) thing);
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
