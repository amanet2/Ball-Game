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
}
