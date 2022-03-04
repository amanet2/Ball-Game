import java.awt.*;

public class dBlockShadows {
    public static void drawShadowBlockFlat(Graphics2D g2, gBlock block) {
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") + block.getInt("toph") == block.getInt("dimh")) {
                GradientPaint gradient = new GradientPaint(
                        block.getInt("coordx") + block.getInt("dimw")/2,
                        block.getInt("coordy") + block.getInt("dimh"),
                        gColors.getWorldColorFromName("shadow1"),
                        block.getInt("coordx") + block.getInt("dimw")/2,
                        block.getInt("coordy") + block.getInt("dimh")
                                + (int)((block.getInt("wallh"))*sSettings.vfxshadowfactor),
                        gColors.getWorldColorFromName("shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(
                        block.getInt("coordx"),
                        block.getInt("coordy") + block.getInt("dimh"),
                        block.getInt("dimw"),
                        (int)(block.getInt("wallh")*sSettings.vfxshadowfactor)
                );
            }
            else if (block.getInt("toph") > 0) {
                GradientPaint gradient = new GradientPaint(
                        block.getInt("coordx") + block.getInt("dimw")/2,
                        block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph"),
                        gColors.getWorldColorFromName("shadow1"),
                        block.getInt("coordx") + block.getInt("dimw")/2,
                        block.getInt("coordy") + block.getInt("dimh")
                                + (int)((block.getInt("dimh") - block.getInt("toph") - block.getInt("toph")
                        )*sSettings.vfxshadowfactor),
                        gColors.getWorldColorFromName("shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(
                        block.getInt("coordx"),
                        block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph"),
                        block.getInt("dimw"),
                        (int)((block.getInt("dimh") - block.getInt("toph"))*sSettings.vfxshadowfactor)
                );
            }
        }
    }
}
