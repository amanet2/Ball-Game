import java.awt.*;

public class dBlockTopsShading {
    public static void drawBlockTopShadingCube(Graphics2D g2, gBlockCube block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            ),
                    eUtils.scaleInt(block.getInt("coordy") ),
                    gColors.getWorldColorFromName("roofoutline1"),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            ),
                    eUtils.scaleInt(block.getInt("coordy") 
                            + block.getInt("toph")),
                    gColors.getWorldColorFromName("roofoutline2")
            );
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(block.getInt("coordx") ),
                    eUtils.scaleInt(block.getInt("coordy") ),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("toph")),
                    eUtils.scaleInt(5),
                    eUtils.scaleInt(5)
            );
        }
    }
}
