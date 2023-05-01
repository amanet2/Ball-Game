import java.awt.*;
import java.awt.geom.Rectangle2D;

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
    
    public static void drawThingShadow(Graphics2D g2, gThing thing) {
        if(sSettings.vfxenableshadows) {
            //check null fields
            if(!thing.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                return;
            int yadj = 5*thing.getInt("dimh")/6;
            Rectangle2D shadowBounds = new Rectangle.Double(
                    thing.getInt("coordx"),
                    thing.getInt("coordy") + yadj,
                    thing.getInt("dimw"),
                    (double)thing.getInt("dimh")/3);
            RadialGradientPaint df = new RadialGradientPaint(
                    shadowBounds, new float[]{0f, 1f},
                    new Color[]{
                            gColors.getColorFromName("clrw_shadow1"),
                            gColors.getColorFromName("clrw_clear")
                    }, MultipleGradientPaint.CycleMethod.NO_CYCLE);
            g2.setPaint(df);
            g2.fillRect((int)shadowBounds.getX(), (int)shadowBounds.getY(), (int)shadowBounds.getWidth(),
                    (int)shadowBounds.getHeight());
        }
    }
}
