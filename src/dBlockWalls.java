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
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCube(g2, (gBlockCube) thing);
                    }
                }
//                else if(thing.get("type").contains("CORNERUR")) {
//                    if (thing.contains("wallh")) {
//                        dBlockShadows.drawShadowBlockCornerUR(g2, (gBlockCornerUR) thing);
//                        drawBlockWallCornerUR(g2, (gBlockCornerUR) thing);
//                    }
//                    if (thing.contains("toph") && thing.isOne("backtop")) {
//                        dBlockTops.drawBlockTopCornerUR(g2, (gBlockCornerUR) thing);
//                    }
//                }
//                else if(thing.get("type").contains("CORNERUL")) {
//                    if (thing.contains("wallh")) {
//                        dBlockShadows.drawShadowBlockCornerUL(g2, (gBlockCornerUL) thing);
//                        drawBlockWallCornerUL(g2, (gBlockCornerUL) thing);
//                    }
//                    if (thing.contains("toph") && thing.isOne("backtop")) {
//                        dBlockTops.drawBlockTopCornerUL(g2, (gBlockCornerUL) thing);
//                    }
//                }
//                else if(thing.get("type").contains("CORNERLL")) {
//                    if (thing.contains("wallh")) {
//                        dBlockShadows.drawShadowBlockFlat(g2, (gBlock) thing);
//                        drawBlockWallCornerLL(g2, (gBlockCornerLL) thing);
//                    }
//                    if (thing.contains("toph") && thing.isOne("backtop")) {
//                        dBlockTops.drawBlockTopCornerLL(g2, (gBlockCornerLL) thing);
//                    }
//                }
//                else if(thing.get("type").contains("CORNERLR")) {
//                    if (thing.contains("wallh")) {
//                        dBlockShadows.drawShadowBlockFlat(g2, (gBlock) thing);
//                        drawBlockWallCornerLR(g2, (gBlockCornerLR) thing);
//                    }
//                    if (thing.contains("toph") && thing.isOne("backtop")) {
//                        dBlockTops.drawBlockTopCornerLR(g2, (gBlockCornerLR) thing);
//                    }
//                }
            }
        }
    }

    public static void drawBlockWallCube(Graphics2D g2, gBlockCube block) {
        if (block.contains("wallh")) {
//            String[] colorvals = block.get("colorwall").split("\\.");
//            g2.setColor(new Color(
//                    Integer.parseInt(colorvals[0]),
//                    Integer.parseInt(colorvals[1]),
//                    Integer.parseInt(colorvals[2]),
//                    Integer.parseInt(colorvals[3])
//            ));
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
            String[] colorvals = block.get("colorwall").split("\\.");
            g2.setColor(new Color(
                    Integer.parseInt(colorvals[0]),
                    Integer.parseInt(colorvals[1]),
                    Integer.parseInt(colorvals[2]),
                    Integer.parseInt(colorvals[3])
            ));
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx")/4),
                    eUtils.scaleInt(block.getInt("coordy")/4+ block.getInt("toph")/4),
                    eUtils.scaleInt(block.getInt("dimw")/4),
                    eUtils.scaleInt(block.getInt("wallh")/4)
            );
//            dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
        }
    }

//    public static void drawBlockWallCornerUR(Graphics2D g2, gBlockCornerUR block) {
//        String[] colorvals = block.get("colorwall").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon pw = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph") + block.getInt("wallh")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy") + block.getInt("wallh"))
//                },
//                4);
//        g2.fillPolygon(pw);
//        dBlockWallsShading.drawBlockWallsShadingCornerUR(g2, block, pw);
//    }
//
//    public static void drawBlockWallCornerLR(Graphics2D g2, gBlockCornerLR block) {
//        String[] colorvals = block.get("colorwall").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon pw = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph") + block.getInt("wallh")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph") + block.getInt("wallh"))
//                },
//                4);
//        g2.fillPolygon(pw);
//        dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
//    }
//
//    public static void drawBlockWallCornerUL(Graphics2D g2, gBlockCornerUL block) {
//        String[] colorvals = block.get("colorwall").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon pw = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("wallh")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph") + block.getInt("wallh")),
//                },
//                4);
//        g2.fillPolygon(pw);
//        dBlockWallsShading.drawBlockWallsShadingCornerUL(g2, block, pw);
//    }
//
//    public static void drawBlockWallCornerLL(Graphics2D g2, gBlockCornerLL block) {
//        String[] colorvals = block.get("colorwall").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon pw = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph") + block.getInt("wallh")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph") + block.getInt("wallh"))
//                },
//                4);
//        g2.fillPolygon(pw);
//        dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
//    }
}
