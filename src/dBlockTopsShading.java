import java.awt.*;

public class dBlockTopsShading {
    public static void drawBlockTopShadingCube(Graphics2D g2, gBlockCube block) {
        g2.setStroke(dFonts.thickStroke);
        g2.setColor(new Color(0, 0, 0, 255));
        if (sSettings.vfxenableshading) {
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0, 0, 0, cVars.getInt("vfxroofoutlinealpha1")),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    new Color(0, 0, 0, cVars.getInt("vfxroofoutlinealpha2")));
            GradientPaint  gradient2 = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0, 0, 0, cVars.getInt("vfxroofshadingalpha1")),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    new Color(0, 0, 0, cVars.getInt("vfxroofshadingalpha2")));
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
