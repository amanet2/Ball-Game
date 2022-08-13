import java.awt.*;

public class dBlockTopsShading {
    public static void drawBlockTopShadingCube(Graphics2D g2, gBlockCube block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    block.getInt("coordx") + block.getInt("dimw") / 2
                            ,
                    block.getInt("coordy") ,
                    gColors.instance().getColorFromName("clrw_roofoutline1"),
                    block.getInt("coordx") + block.getInt("dimw") / 2
                            ,
                    block.getInt("coordy")
                            + block.getInt("toph"),
                    gColors.instance().getColorFromName("clrw_roofoutline2")
            );
            g2.setPaint(gradient);
            g2.drawRoundRect(
                    block.getInt("coordx") ,
                    block.getInt("coordy") ,
                    block.getInt("dimw"),
                    block.getInt("toph"),
                    5,
                    5
            );
        }
    }
}
