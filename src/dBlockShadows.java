import java.awt.*;

public class dBlockShadows {
    public static void drawShadowBlockFlat(Graphics2D g2, gBlock block) {
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") + block.getInt("toph") == block.getInt("dimh")) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh")),
                        gColors.getWorldColorFromName("shadow1"),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh")
                                + (int)((block.getInt("wallh"))*sSettings.vfxshadowfactor)),
                        gColors.getWorldColorFromName("shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")
                                + block.getInt("dimh")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt((int)(block.getInt("wallh")*sSettings.vfxshadowfactor))
                );
            }
            else if (block.getInt("toph") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh") - block.getInt("toph")),
                        gColors.getWorldColorFromName("shadow1"),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh")
                                + (int)((block.getInt("dimh") - block.getInt("toph") - block.getInt("toph")
                        )*sSettings.vfxshadowfactor)),
                        gColors.getWorldColorFromName("shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")
                                + block.getInt("dimh") - block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt((int)((block.getInt("dimh") - block.getInt("toph")
                        )*sSettings.vfxshadowfactor))
                );
            }
        }
    }
}
