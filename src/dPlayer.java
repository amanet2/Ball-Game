import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dPlayer {
    public static void drawPlayers(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            for (gPlayer e : eManager.currentMap.scene.players()) {
                //player gun
                if(e.getInt("weapon") != gWeapons.type.NONE.code()) {
                    int[] rgb = new int[4];
                    rgb[3] = 255;
                    switch (e.getInt("weapon")) {
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
                    if(e.getInt("weapon") != gWeapons.type.NONE.code()) {
                        int x = eUtils.scaleInt(e.getInt("coordx")-cVars.getInt("camx")
                                - e.getInt("dimw")/4);
                        int y = eUtils.scaleInt(e.getInt("coordy")-cVars.getInt("camy") - e.getInt("dimh")/4);
                        int w = eUtils.scaleInt(3*e.getInt("dimw")/2);
                        int h = eUtils.scaleInt(3*e.getInt("dimh")/2);
                        if(sVars.isOne("vfxenableflares"))
                            dFlares.drawFlare(g2,x,y,w,h,1,rgb,new int[4]);
                        //solid ring
                        g2.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                        g2.fillOval(
                                eUtils.scaleInt(e.getInt("coordx") - xCon.getInt("cv_camx") - e.getInt("dimw")/16),
                                eUtils.scaleInt(e.getInt("coordy") - xCon.getInt("cv_camy") - e.getInt("dimw")/16),
                                eUtils.scaleInt(e.getInt("dimw") + e.getInt("dimw")/8),
                                eUtils.scaleInt(e.getInt("dimh") + e.getInt("dimw")/8)
                        );
                    }
                }
                //player sickness
                if(e.isOne("sicknessfast")) {
                    g2.setColor(new Color(255,255,190));
                    g2.fillOval(
                            eUtils.scaleInt(e.getInt("coordx") - xCon.getInt("cv_camx") - e.getInt("dimw")/16),
                            eUtils.scaleInt(e.getInt("coordy") - xCon.getInt("cv_camy") - e.getInt("dimw")/16),
                            eUtils.scaleInt(e.getInt("dimw") + e.getInt("dimw")/8),
                            eUtils.scaleInt(e.getInt("dimh") + e.getInt("dimw")/8)
                    );
                }
                if(e.isOne("sicknessslow")) {
                    g2.setColor(new Color(80,40,0));
                    g2.fillOval(
                            eUtils.scaleInt(e.getInt("coordx") - xCon.getInt("cv_camx") - e.getInt("dimw")/16),
                            eUtils.scaleInt(e.getInt("coordy") - xCon.getInt("cv_camy") - e.getInt("dimw")/16),
                            eUtils.scaleInt(e.getInt("dimw") + e.getInt("dimw")/8),
                            eUtils.scaleInt(e.getInt("dimh") + e.getInt("dimw")/8)
                    );
                }
                //player shadow
                if (cVars.getInt("mapview") == gMap.MAP_TOPVIEW) {
                    if(sVars.isOne("vfxenableshadows")) {
                        int yadj = 5*e.getInt("dimh")/6 + cVars.getInt("jumpheight");
                        Rectangle2D shadowBounds = new Rectangle.Double(
                                eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")
                                        + yadj),
                                eUtils.scaleInt(e.getInt("dimw")),
                                eUtils.scaleInt(e.getInt("dimh")/3));
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
                }
                //flag for ctf
                if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER) &&
                (cVars.isVal("flagmasterid", e.get("id").length() > 0 ? e.get("id") : uiInterface.uuid))) {
                    gProp flag = null;
                    HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                    for(String id : thingMap.keySet()) {
                        flag = (gProp) thingMap.get(id);
                    }
                    if(flag != null) {
                        g2.drawImage(flag.sprite,
                                eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")
                                        - e.getInt("dimw")/2),
                                eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")
                                - e.getInt("dimh")),
                                null
                        );
                    }
                }
                //player itself
                g2.drawImage(
                    e.sprite,
                    eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")),
                    null
                );
                //shading
                if(sVars.isOne("vfxenableshading")) {
                    GradientPaint df = new GradientPaint(
                            eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(e.getInt("coordy") + 2*e.getInt("dimh")/3- cVars.getInt("camy")),
                            new Color(0,0, 0,0),
                            eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(e.getInt("coordy") + e.getInt("dimh") - cVars.getInt("camy")),
                            new Color(0,0, 0,cVars.getInt("vfxshadowalpha1")/2));
                    g2.setPaint(df);
                    g2.fillOval(eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(e.getInt("dimw")),
                            eUtils.scaleInt(e.getInt("dimh")));
                }
                //player weapon
                AffineTransform backup = g2.getTransform();
                AffineTransform a = g2.getTransform();
                a.rotate(e.getDouble("fv")-Math.PI/2,
                        eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx") + e.getInt("dimw") / 2),
                        eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy") + e.getInt("dimh") / 2)
                );
                g2.setTransform(a);
                int diff = e.getDouble("fv") >= 2*Math.PI || e.getDouble("fv") <= Math.PI ?
                        gWeapons.fromCode(e.getInt("weapon")).dims[1]/2:
                        gWeapons.fromCode(e.getInt("weapon")).dims[1]/2;
                g2.drawImage(gWeapons.fromCode(e.getInt("weapon")).sprite,
                        eUtils.scaleInt(e.getInt("coordx")+ e.getInt("dimw")/2-cVars.getInt("camx")),
                        eUtils.scaleInt(e.getInt("coordy")+ e.getInt("dimh")/2-cVars.getInt("camy")-diff),
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
