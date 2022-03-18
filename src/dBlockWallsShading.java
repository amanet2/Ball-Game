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
                            gColors.getWorldColorFromName("walllowshading1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getWorldColorFromName("walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.getWorldColorFromName("wallshading1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getWorldColorFromName("wallshading2")
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
                            gColors.getWorldColorFromName("walllowoutline1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getWorldColorFromName("walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.getWorldColorFromName("walloutline1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getWorldColorFromName("walloutline2")
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
