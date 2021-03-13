import java.awt.*;

public class dTileTopsCorners {
    public static void drawTileCorners(Graphics2D g2, gTile t) {
        int d6w = t.getInt("dim6w");
        if(d6w > -1)
            return;
        if(d6w == -1) { //UR
            Polygon p = new Polygon(
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                    + t.getInt("dimw")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                    + t.getInt("dimw"))
                    },
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh"))
                    },
                    3);
            Shape b = g2.getClip();
            g2.setClip(p);
            g2.drawImage(t.spriteT,
                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    null
            );
            if(sVars.isOne("vfxenableshading")) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                + t.getInt("dimh")),
                        new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                g2.setPaint(gradient);
                g2.drawPolygon(p);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")),
                        eUtils.scaleInt(t.getInt("dimh"))
                );
            }
            g2.setClip(b);
        }
        else if(d6w == -2) { //BR
            Polygon p = new Polygon(
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                    + t.getInt("dimw")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                    + t.getInt("dimw")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                    },
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh"))
                    },
                    3);
            Shape b = g2.getClip();
            g2.setClip(p);
            g2.drawImage(t.spriteT,
                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    null
            );
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            if(sVars.isOne("vfxenableshading")) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                + t.getInt("dimh")),
                        new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                g2.setPaint(gradient);
                g2.drawPolygon(p);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")),
                        eUtils.scaleInt(t.getInt("dimh"))
                );
            }
            g2.setClip(b);
        }
        else if(d6w == -3) { //BL
            Polygon p = new Polygon(
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                    + t.getInt("dimw")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                    },
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh"))
                    },
                    3);
            Shape b = g2.getClip();
            g2.setClip(p);
            g2.drawImage(t.spriteT,
                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    null
            );
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            if(sVars.isOne("vfxenableshading")) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                + t.getInt("dimh")),
                        new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                g2.setPaint(gradient);
                g2.drawPolygon(p);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")),
                        eUtils.scaleInt(t.getInt("dimh"))
                );
            }
            g2.setClip(b);
        }
        else if(d6w == -4) { //UL
            Polygon p = new Polygon(
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                    + t.getInt("dimw")),
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                    },
                    new int[]{
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh"))
                    },
                    3);
            Shape b = g2.getClip();
            g2.setClip(p);
            g2.drawImage(t.spriteT,
                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    null
            );
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            if(sVars.isOne("vfxenableshading")) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                + t.getInt("dimh")),
                        new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                g2.setPaint(gradient);
                g2.drawPolygon(p);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")),
                        eUtils.scaleInt(t.getInt("dimh"))
                );
            }
            g2.setClip(b);
        }
    }
}
