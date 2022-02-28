import java.awt.*;

public class dBlockTopsShading {
    public static void drawBlockTopShadingCube(Graphics2D g2, gBlockCube block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    gColors.getWorldColorFromName("roofoutline1"),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    gColors.getWorldColorFromName("roofoutline2")
            );
            GradientPaint  gradient2 = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    gColors.getWorldColorFromName("roofshading1"),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    gColors.getWorldColorFromName("roofshading2")
            );
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("toph")),
                    eUtils.scaleInt(5),
                    eUtils.scaleInt(5)
            );
            g2.setPaint(gradient2);
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("toph"))
            );
        }
    }
}
