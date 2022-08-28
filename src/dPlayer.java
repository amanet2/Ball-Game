import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.MultipleGradientPaint;
import java.awt.geom.Rectangle2D;

public class dPlayer {
    public static void drawPlayer(Graphics2D g2, gPlayer player) {
        //player glow
        if(player == null)
            return;
        if(!player.contains("id"))
            return;
        nState cState = nClient.instance().clientStateMap.get(player.get("id"));
        if(cState == null)
            return;
        if(cState.contains("color")) {
            Color pc = gColors.instance().getColorFromName("clrp_" + cState.get("color"));
            if (pc != null) {
                int x = player.getInt("coordx") - player.getInt("dimw") / 4;
                int y = player.getInt("coordy") - player.getInt("dimh") / 4;
                int w = 3 * player.getInt("dimw") / 2;
                int h = 3 * player.getInt("dimh") / 2;
                if (sSettings.vfxenableflares)
                    dFlares.drawFlareFromColor(g2, x, y, w, h, 1, pc, new Color(0, 0, 0, 0));
            }
        }
        //player shadow
        if(sSettings.vfxenableshadows) {
            //check null fields
            if(!player.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                return;
            int yadj = 5*player.getInt("dimh")/6;
            Rectangle2D shadowBounds = new Rectangle.Double(
                    player.getInt("coordx"),
                    player.getInt("coordy") + yadj,
                    player.getInt("dimw"),
                    (double)player.getInt("dimh")/3);
            RadialGradientPaint df = new RadialGradientPaint(
                    shadowBounds, new float[]{0f, 1f},
                    new Color[]{
                            gColors.instance().getColorFromName("clrw_shadow1"),
                            gColors.instance().getColorFromName("clrw_clear")
                    }, MultipleGradientPaint.CycleMethod.NO_CYCLE);
            g2.setPaint(df);
            g2.fillRect((int)shadowBounds.getX(), (int)shadowBounds.getY(), (int)shadowBounds.getWidth(),
                    (int)shadowBounds.getHeight());
        }
        //player itself
        g2.drawImage(
                player.sprite,
                player.getInt("coordx"),
                player.getInt("coordy"),
                null
        );
        //flag for ctf
        if(nClient.instance().serverArgsMap.containsKey("flagmasterid")
                && nClient.instance().serverArgsMap.get("flagmasterid").equals(player.get("id"))) {
            g2.drawImage(gItemFactory.flagSprite, player.getInt("coordx"),
                    player.getInt("coordy") - 2*player.getInt("dimh")/3, null
            );
        }
        //shading
        if(sSettings.vfxenableshading) {
            GradientPaint df = new GradientPaint(
                    player.getInt("coordx"),
                    player.getInt("coordy") + 2*player.getInt("dimh")/3,
                    gColors.instance().getColorFromName("clrw_clear"),
                    player.getInt("coordx"),
                    player.getInt("coordy") + player.getInt("dimh"),
                    gColors.instance().getColorFromName("clrw_shadow1half")
            );
            g2.setPaint(df);
            g2.fillOval(
                    player.getInt("coordx"),
                    player.getInt("coordy"),
                    player.getInt("dimw"),
                    player.getInt("dimh")
            );
        }
        //player weapon
        AffineTransform backup = g2.getTransform();
        AffineTransform a = g2.getTransform();
        a.rotate(player.getDouble("fv")-Math.PI/2,
                player.getInt("coordx") + (float) player.getInt("dimw") / 2,
                player.getInt("coordy") + (float) player.getInt("dimh") / 2
        );
        g2.setTransform(a);
        int diff = gWeapons.fromCode(player.getInt("weapon")).dims[1] / 2;
        g2.drawImage(gWeapons.fromCode(player.getInt("weapon")).sprite,
                player.getInt("coordx") + player.getInt("dimw")/2,
                player.getInt("coordy") + player.getInt("dimh")/2 - diff,
                null);
        g2.setTransform(backup);
    }
}
