import java.awt.*;

public class dBlockWalls {
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
        dBlockWallsShading.drawBlockWallsShadingCorner(g2, block, pw);
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
        dBlockWallsShading.drawBlockWallsShadingCorner(g2, block, pw);
    }
}
