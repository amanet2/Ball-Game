import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class dPlayer {
    public static void drawPlayer(Graphics2D g2, gPlayer player) {
        //player glow
        Color pc = gColors.getPlayerHudColorFromName(nClient.instance().serverArgsMap.get(player.get("id")).get("color"));
        if(pc != null) {
            int x = player.getInt("coordx") - player.getInt("dimw")/4;
            int y = player.getInt("coordy") - player.getInt("dimh")/4;
            int w = 3*player.getInt("dimw")/2;
            int h = 3*player.getInt("dimh")/2;
            if(sSettings.vfxenableflares)
                dFlares.drawFlareFromColor(g2,x,y,w,h,1,pc, new Color(0,0,0,0));
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
                            gColors.getWorldColorFromName("shadow1"),
                            gColors.getWorldColorFromName("clear")
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
        if(nClient.instance().serverArgsMap.containsKey("server")
                && nClient.instance().serverArgsMap.get("server").containsKey("flagmasterid")
                && nClient.instance().serverArgsMap.get("server").get("flagmasterid").equals(player.get("id"))) {
            g2.drawImage(gItemFactory.instance().flagSprite,
                    player.getInt("coordx"),
                    player.getInt("coordy")
                            - 2*player.getInt("dimh")/3,
                    null
            );
        }
        //shading
        if(sSettings.vfxenableshading) {
            GradientPaint df = new GradientPaint(
                    player.getInt("coordx"),
                    player.getInt("coordy") + 2*player.getInt("dimh")/3,
                    gColors.getWorldColorFromName("clear"),
                    player.getInt("coordx"),
                    player.getInt("coordy") + player.getInt("dimh"),
                    gColors.getWorldColorFromName("shadow1half")
            );
            g2.setPaint(df);
            g2.fillOval(
                    player.getInt("coordx"),
                    player.getInt("coordy"),
                    player.getInt("dimw"),
                    player.getInt("dimh")
            );
        }
//        //player weapon
//        AffineTransform backup = g2.getTransform();
//        AffineTransform a = g2.getTransform();
//        a.rotate(player.getDouble("fv")-Math.PI/2,
//                eUtils.scaleInt(player.getInt("coordx")
//                        + player.getInt("dimw") / 2),
//                eUtils.scaleInt(player.getInt("coordy")
//                        + player.getInt("dimh") / 2)
//        );
//        g2.setTransform(a);
//        int diff = player.getDouble("fv") >= 2*Math.PI || player.getDouble("fv") <= Math.PI ?
//                gWeapons.fromCode(player.getInt("weapon")).dims[1]/2:
//                gWeapons.fromCode(player.getInt("weapon")).dims[1]/2;
//        g2.drawImage(gWeapons.fromCode(player.getInt("weapon")).sprite,
//                eUtils.scaleInt(player.getInt("coordx") + player.getInt("dimw")/2
//                       ),
//                eUtils.scaleInt(player.getInt("coordy") + player.getInt("dimh")/2
//                       -diff),
//                null);
//        g2.setTransform(backup);
    }
}
