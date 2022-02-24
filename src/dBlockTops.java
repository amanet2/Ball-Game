import java.awt.*;
import java.util.HashMap;

public class dBlockTops {
    public static void drawBlockTopCube(Graphics2D g2, gBlockCube block) {
        String[] colorvals = block.get("color").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                eUtils.scaleInt(block.getInt("dimw")),
                eUtils.scaleInt(block.getInt("toph"))
        );
        dBlockTopsShading.drawBlockTopShadingCube(g2, block);
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        HashMap<String, gThing> squareMap = uiEditorMenus.previewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockWalls.drawBlockWallCubePreview(g2, block);
            }
        }
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("toph")) {
                dBlockTops.drawBlockTopCubePreview(g2, block);
            }
        }
    }

    public static void drawBlockTopCubePreview(Graphics2D g2, gBlockCube block) {
        String[] colorvals = block.get("color").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        g2.fillRect(eUtils.scaleInt(block.getInt("coordx")/4),
                eUtils.scaleInt(block.getInt("coordy")/4),
                eUtils.scaleInt(block.getInt("dimw")/4),
                eUtils.scaleInt(block.getInt("toph")/4)
        );
    }
//
//    public static void drawBlockTopCornerUR(Graphics2D g2, gBlockCornerUR block) {
//        String[] colorvals = block.get("color").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon p = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph"))
//                },
//                3);
//        g2.fillPolygon(p);
//        dBlockTopsShading.drawBlockTopShadingCorner(g2, block, p);
//    }
//
//    public static void drawBlockTopCornerUL(Graphics2D g2, gBlockCornerUL block) {
//        String[] colorvals = block.get("color").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon p = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph"))
//                },
//                3);
//        g2.fillPolygon(p);
//        dBlockTopsShading.drawBlockTopShadingCorner(g2, block, p);
//    }
//
//    public static void drawBlockTopCornerLR(Graphics2D g2, gBlockCornerLR block) {
//        String[] colorvals = block.get("color").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon p = new Polygon(
//                new int[]{
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
//                                + block.getInt("toph"))
//                },
//                3);
//        g2.fillPolygon(p);
//        dBlockTopsShading.drawBlockTopShadingCorner(g2, block, p);
//    }
//
//    public static void drawBlockTopCornerLL(Graphics2D g2, gBlockCornerLL block) {
//        String[] colorvals = block.get("color").split("\\.");
//        g2.setColor(new Color(
//                Integer.parseInt(colorvals[0]),
//                Integer.parseInt(colorvals[1]),
//                Integer.parseInt(colorvals[2]),
//                Integer.parseInt(colorvals[3])
//        ));
//        Polygon p = new Polygon(
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
//                                + block.getInt("dimw")),
//                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
//                },
//                new int[]{
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("toph"))
//                },
//                3);
//        g2.fillPolygon(p);
//        dBlockTopsShading.drawBlockTopShadingCorner(g2, block, p);
//    }
}
