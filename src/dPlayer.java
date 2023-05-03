import java.awt.*;
import java.awt.geom.AffineTransform;

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
            Color pc = gColors.getColorFromName("clrp_" + cState.get("color"));
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
        dBlockShadows.drawThingShadow(g2, player);
        //player itself
        g2.drawImage(
                player.sprite,
                player.getInt("coordx"),
                player.getInt("coordy"),
                null
        );
        String decor = player.get("decorationsprite");
        if(!decor.equalsIgnoreCase("null")) {
            g2.drawImage(
                    gTextures.getGScaledImage(eManager.getPath(decor), 300, 300),
                    player.getInt("coordx"), player.getInt("coordy") - 2*player.getInt("dimh")/3,
                    null
            );
        }
        //shading
        if(sSettings.vfxenableshading) {
            GradientPaint df = new GradientPaint(
                    player.getInt("coordx"),
                    player.getInt("coordy") + 2*player.getInt("dimh")/3,
                    gColors.getColorFromName("clrw_clear"),
                    player.getInt("coordx"),
                    player.getInt("coordy") + player.getInt("dimh"),
                    gColors.getColorFromName("clrw_shadow1half")
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
