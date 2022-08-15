import java.awt.*;

public class dBlockWallsShading {
    public static void drawBlockWallsShadingFlat(Graphics2D g2, gBlock block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient;
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.instance().getColorFromName("clrw_walllowshading1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.instance().getColorFromName("clrw_walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.instance().getColorFromName("clrw_wallshading1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.instance().getColorFromName("clrw_wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(block.getX(), block.getY() + block.getInt("toph"), block.getWidth(),
                            block.getInt("wallh")
                );
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.instance().getColorFromName("clrw_walllowoutline1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.instance().getColorFromName("clrw_walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.instance().getColorFromName("clrw_walloutline1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.instance().getColorFromName("clrw_walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(block.getX(), block.getY() + block.getInt("toph"),
                                 block.getWidth(), block.getInt("wallh"), 5, 5
                );
            }
        }
    }
}
