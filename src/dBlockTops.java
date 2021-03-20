import java.awt.*;

public class dBlockTops {
    public static void drawBlockTopCube(Graphics2D g2, gBlockCube block) {
        String[] colorvals = block.get("color").split(",");
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

    public static void drawBlockTopCornerUR(Graphics2D g2, gBlockCornerUR block) {
        String[] colorvals = block.get("color").split(",");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon p = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph"))
                },
                3);
        g2.fillPolygon(p);
        dBlockTopsShading.drawBlockTopShadingCorner(g2, block, p);
    }

    public static void drawBlockTopCornerUL(Graphics2D g2, gBlockCornerUL block) {
        String[] colorvals = block.get("color").split(",");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon p = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph"))
                },
                3);
        g2.fillPolygon(p);
        dBlockTopsShading.drawBlockTopShadingCorner(g2, block, p);
    }
}
