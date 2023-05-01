import java.awt.*;
import java.awt.geom.Rectangle2D;

public class dItems {
    public static void drawItem(Graphics2D g2, gItem item) {
        if(item.sprite != null) {
            //player shadow
            if(sSettings.vfxenableshadows) {
                //check null fieldss
                if(!item.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                    return;
                int yadj = 5*item.getInt("dimh")/6;
                Rectangle2D shadowBounds = new Rectangle.Double(
                        item.getInt("coordx"),
                        item.getInt("coordy") + yadj,
                        item.getInt("dimw"),
                        (double)item.getInt("dimh")/3);
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
            g2.drawImage(item.sprite,
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    null
            );
            if(sSettings.vfxenableflares && !item.get("flare").equals("null")) {
                String[] flareToks = item.get("flare").split(":");
                int[] flareArgs = new int[] {
                        Integer.parseInt(flareToks[0]),
                        Integer.parseInt(flareToks[1]),
                        Integer.parseInt(flareToks[2]),
                        Integer.parseInt(flareToks[3])
                };
                dFlares.drawFlare(g2,
                        item.getInt("coordx") - item.getInt("dimw")/2,
                        item.getInt("coordy") - item.getInt("dimh")/2,
                        item.getInt("dimw")*2,
                        item.getInt("dimh")*2,
                        1, new int[]{flareArgs[0], flareArgs[1], flareArgs[2], flareArgs[3]}, new int[4]
                );
            }
        }
        else if(sSettings.show_mapmaker_ui){
            dFonts.setFontColor(g2, "clrf_spawnpoint");
            g2.fillRect(
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    item.getInt("dimw"),
                    item.getInt("dimh"));
        }
    }
}
