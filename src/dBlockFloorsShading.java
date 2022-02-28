import java.awt.*;

public class dBlockFloorsShading {
    public static void drawBlockFloorShading(Graphics2D g2, gBlockFloor block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    gColors.getWorldColorFromName("floorshading1"),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2 - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy") + block.getInt("dimh")),
                    gColors.getWorldColorFromName("floorshading2")
            );
            GradientPaint gradient2 = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    gColors.getWorldColorFromName("flooroutline1"),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("dimh")),
                    gColors.getWorldColorFromName("flooroutline2")
            );
            g2.setPaint(gradient2);
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("dimh"))
            );
            g2.setStroke(dFonts.thickStroke);
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("dimh")),
                    eUtils.scaleInt(5), eUtils.scaleInt(5)
            );
        }
    }
}
