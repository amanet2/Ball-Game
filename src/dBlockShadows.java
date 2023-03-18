import java.awt.Graphics2D;
import java.awt.GradientPaint;

public class dBlockShadows {
    public static void drawShadowBlockFlat(Graphics2D g2, gBlock block) {
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") + block.getInt("toph") == block.getHeight()) {
                GradientPaint gradient = new GradientPaint(
                        block.getX() + block.getWidth()/2,block.getY() + block.getHeight(),
                        gColors.getColorFromName("clrw_shadow1"),
                        block.getX() + block.getWidth()/2,
                        block.getY() + block.getHeight()
                                + (int)((block.getInt("wallh"))*sSettings.vfxshadowfactor),
                        gColors.getColorFromName("clrw_shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(
                        block.getX(),
                        block.getY() + block.getHeight(),
                        block.getWidth(),
                        (int)(block.getInt("wallh")*sSettings.vfxshadowfactor)
                );
            }
            else if (block.getInt("toph") > 0) {
                GradientPaint gradient = new GradientPaint(
                        block.getX() + block.getWidth()/2,
                        block.getY() + block.getHeight() - block.getInt("toph"),
                        gColors.getColorFromName("clrw_shadow1"),
                        block.getX() + block.getWidth()/2,
                        block.getY() + block.getHeight()
                                + (int)((block.getHeight() - block.getInt("toph") - block.getInt("toph")
                        )*sSettings.vfxshadowfactor),
                        gColors.getColorFromName("clrw_shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(block.getX(), block.getY() + block.getHeight() - block.getInt("toph"),
                        block.getWidth(), (int)((block.getHeight() - block.getInt("toph"))*sSettings.vfxshadowfactor)
                );
            }
        }
    }
}
