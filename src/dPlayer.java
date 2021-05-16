import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class dPlayer {
    public static void drawPlayers(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            for (String playerId : cClientLogic.getPlayerIds()) {
                gPlayer player = cClientLogic.getPlayerById(playerId);
                //player gun
                if(player.getInt("weapon") != gWeapons.type.NONE.code()) {
                    int[] rgb = new int[4];
                    rgb[3] = 255;
                    switch (player.getInt("weapon")) {
                        case gWeapons.type.pistol:
                            rgb[0] = 100;
                            rgb[1] = 255;
                            break;
                        case gWeapons.type.shotgun:
                            rgb[1] = 200;
                            rgb[2] = 255;
                            break;
                        case gWeapons.type.autorifle:
                            rgb[0] = 255;
                            rgb[1] = 170;
                            break;
                        case gWeapons.type.launcher:
                            rgb[0] = 255;
                            break;
                        case gWeapons.type.gloves:
                            rgb[0] = 255;
                            rgb[1] = 255;
                            break;
                        default:
                            break;
                    }
                    if(player.getInt("weapon") != gWeapons.type.NONE.code()) {
                        int x = eUtils.scaleInt(player.getInt("coordx")-cVars.getInt("camx")
                                - player.getInt("dimw")/4);
                        int y = eUtils.scaleInt(player.getInt("coordy")-cVars.getInt("camy")
                                - player.getInt("dimh")/4);
                        int w = eUtils.scaleInt(3*player.getInt("dimw")/2);
                        int h = eUtils.scaleInt(3*player.getInt("dimh")/2);
                        if(sVars.isOne("vfxenableflares"))
                            dFlares.drawFlare(g2,x,y,w,h,1,rgb,new int[4]);
                        //solid ring
                        g2.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                        g2.fillOval(
                                eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")
                                        - player.getInt("dimw")/16),
                                eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")
                                        - player.getInt("dimw")/16),
                                eUtils.scaleInt(player.getInt("dimw") + player.getInt("dimw")/8),
                                eUtils.scaleInt(player.getInt("dimh") + player.getInt("dimw")/8)
                        );
                    }
                }
                //player shadow
                if(sVars.isOne("vfxenableplayershadow")) {
                    //check null fields
                    if(!player.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                        break;
                    int yadj = 5*player.getInt("dimh")/6;
                    Rectangle2D shadowBounds = new Rectangle.Double(
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")
                                    + yadj),
                            eUtils.scaleInt(player.getInt("dimw")),
                            eUtils.scaleInt(player.getInt("dimh")/3));
                    RadialGradientPaint df = new RadialGradientPaint(
                            shadowBounds, new float[]{0f, 1f},
                            new Color[]{
                                    new Color(0,0, 0, cVars.getInt("vfxshadowalpha1")),
                                    new Color(0, 0, 0, 0)
                            }, MultipleGradientPaint.CycleMethod.NO_CYCLE);
                    g2.setPaint(df);
                    g2.fillRect((int)shadowBounds.getX(), (int)shadowBounds.getY(), (int)shadowBounds.getWidth(),
                            (int)shadowBounds.getHeight());
                }
                //flag for ctf
                if(nClient.instance().serverArgsMap.containsKey("server")
                && nClient.instance().serverArgsMap.get("server").containsKey("flagmasterid")
                && nClient.instance().serverArgsMap.get("server").get("flagmasterid").equals(playerId)) {
                    g2.drawImage(gItemFactory.instance().flagSprite,
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")
                                    - player.getInt("dimw")/2),
                            eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")
                                    - player.getInt("dimh")),
                            null
                    );
                }
                //player itself
                g2.drawImage(
                    player.sprite,
                    eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")),
                    null
                );
                //shading
                if(sVars.isOne("vfxenableshading")) {
                    GradientPaint df = new GradientPaint(
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") + 2*player.getInt("dimh")/3
                                    - cVars.getInt("camy")),
                            new Color(0,0, 0,0),
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") + player.getInt("dimh")
                                    - cVars.getInt("camy")),
                            new Color(0,0, 0,cVars.getInt("vfxshadowalpha1")/2));
                    g2.setPaint(df);
                    g2.fillOval(eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(player.getInt("dimw")),
                            eUtils.scaleInt(player.getInt("dimh")));
                }
                //player weapon
                AffineTransform backup = g2.getTransform();
                AffineTransform a = g2.getTransform();
                a.rotate(player.getDouble("fv")-Math.PI/2,
                        eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")
                                + player.getInt("dimw") / 2),
                        eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")
                                + player.getInt("dimh") / 2)
                );
                g2.setTransform(a);
                int diff = player.getDouble("fv") >= 2*Math.PI || player.getDouble("fv") <= Math.PI ?
                        gWeapons.fromCode(player.getInt("weapon")).dims[1]/2:
                        gWeapons.fromCode(player.getInt("weapon")).dims[1]/2;
                g2.drawImage(gWeapons.fromCode(player.getInt("weapon")).sprite,
                        eUtils.scaleInt(player.getInt("coordx") + player.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(player.getInt("coordy") + player.getInt("dimh")/2
                                - cVars.getInt("camy")-diff),
                        null);
                g2.setTransform(backup);
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }
}
