import java.awt.*;
import java.util.HashMap;

public class dBlockWalls {
    public static void drawBlockWalls(Graphics2D g2) {
        HashMap<String, gThing> squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockFlat(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCube(g2, block);
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCube(g2, block);
            }
        }
        squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERUR");
        for(String tag : squareMap.keySet()) {
            gBlockCornerUR block = (gBlockCornerUR) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockCornerUR(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerUR(g2, block);
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCornerUR(g2, block);
            }
        }
        squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERUL");
        for(String tag : squareMap.keySet()) {
            gBlockCornerUL block = (gBlockCornerUL) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockCornerUL(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerUL(g2, block);
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCornerUL(g2, block);
            }
        }
        squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERLR");
        for(String tag : squareMap.keySet()) {
            gBlockCornerLR block = (gBlockCornerLR) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockFlat(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerLR(g2, block);
            }
            if(block.contains("toph") && block.isOne("backtop")) {
//                dBlockTops.drawBlockTopCornerLR(g2, block);
            }
        }
        squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERLL");
        for(String tag : squareMap.keySet()) {
            gBlockCornerLL block = (gBlockCornerLL) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockFlat(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerLL(g2, block);
            }
            if(block.contains("toph") && block.isOne("backtop")) {
//                dBlockTops.drawBlockTopCornerLL(g2, block);
            }
        }
    }

    public static void drawBlockWallCube(Graphics2D g2, gBlockCube block) {
        if (block.contains("wallh")) {
            String[] colorvals = block.get("colorwall").split(",");
            g2.setColor(new Color(
                    Integer.parseInt(colorvals[0]),
                    Integer.parseInt(colorvals[1]),
                    Integer.parseInt(colorvals[2]),
                    Integer.parseInt(colorvals[3])
            ));
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("wallh"))
            );
            dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
        }
    }

    public static void drawBlockWallCornerUR(Graphics2D g2, gBlockCornerUR block) {
        String[] colorvals = block.get("colorwall").split(",");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy") + block.getInt("wallh"))
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingCornerUR(g2, block, pw);
    }

    public static void drawBlockWallCornerLR(Graphics2D g2, gBlockCornerLR block) {
        String[] colorvals = block.get("colorwall").split(",");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh"))
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
    }

    public static void drawBlockWallCornerUL(Graphics2D g2, gBlockCornerUL block) {
        String[] colorvals = block.get("colorwall").split(",");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingCornerUL(g2, block, pw);
    }

    public static void drawBlockWallCornerLL(Graphics2D g2, gBlockCornerLL block) {
        String[] colorvals = block.get("colorwall").split(",");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh"))
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
    }
}
