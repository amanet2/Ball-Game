import java.awt.*;

public class dBlockShadows {
    public static void drawShadowBlockCornerUL(Graphics2D g2, gBlockCornerUL block) {
        if (sVars.isOne("vfxenableshadows")) {
            Polygon ps = new Polygon(
                    new int[]{
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                    + block.getInt("dimw")),
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                    + block.getInt("dimw")),
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                    },
                    new int[]{
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh") - block.getInt("toph")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh") - block.getInt("toph")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh") - block.getInt("toph")
                                    + (int) (block.getInt("wallh") * cVars.getDouble("vfxshadowfactor"))),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh")
                                    + (int) (block.getInt("wallh") * cVars.getDouble("vfxshadowfactor")))
                    },
                    4);
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("dimh") - block.getInt("toph")),
                    new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha1")),
                    eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                            + block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("dimh")
                            + (int) (block.getInt("wallh") * cVars.getDouble("vfxshadowfactor"))),
                    new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha2"))
            );
            g2.setPaint(gradient);
            g2.fillPolygon(ps);
        }
    }

    public static void drawShadowBlockCornerUR(Graphics2D g2, gBlockCornerUR block) {
        if (sVars.isOne("vfxenableshadows")) {
            Polygon ps = new Polygon(
                    new int[]{
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                    + block.getInt("dimw")),
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                    + block.getInt("dimw")),
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                    },
                    new int[]{
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh") - block.getInt("toph")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh")
                                    + (int) (block.getInt("wallh") * cVars.getDouble("vfxshadowfactor"))),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh") - block.getInt("toph")
                                    + (int) (block.getInt("wallh") * cVars.getDouble("vfxshadowfactor"))),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh") - block.getInt("toph"))
                    },
                    4);
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                            + block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("dimh") - block.getInt("toph")),
                    new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha1")),
                    eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("dimh")
                            + (int) (block.getInt("wallh") * cVars.getDouble("vfxshadowfactor"))),
                    new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha2"))
            );
            g2.setPaint(gradient);
            g2.fillPolygon(ps);
        }    
    }
    
    public static void drawShadowBlockFlat(Graphics2D g2, gBlock block) {
        if(sVars.isOne("vfxenableshadows")) {
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        new Color(0,0,0,cVars.getInt("vfxshadowalpha1")),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")
                                + (int)(block.getInt("wallh")*cVars.getDouble("vfxshadowfactor"))),
                        new Color(0,0,0, cVars.getInt("vfxshadowalpha2")));
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")
                                + block.getInt("toph")
                                +block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt((int)(block.getInt("wallh")*cVars.getDouble("vfxshadowfactor")))
                );
            } else if (block.getInt("toph") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh")),
                        new Color(0,0,0,cVars.getInt("vfxshadowalpha1")),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh")
                                + (int)(block.getInt("dimh")*cVars.getDouble("vfxshadowfactor"))),
                        new Color(0,0,0, cVars.getInt("vfxshadowalpha2")));
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")
                                + block.getInt("dimh")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt((int)(block.getInt("dimh")*cVars.getDouble("vfxshadowfactor")))
                );
                //
                // 2x gradient below
                //
//                GradientPaint gradient = new GradientPaint(
//                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
//                                - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("dimh")),
//                        new Color(0,0,0,cVars.getInt("vfxshadowalpha2")),
//                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
//                                - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("dimh")
//                                + (int)(block.getInt("toph")*cVars.getDouble("vfxshadowfactor"))),
//                        new Color(0,0,0, cVars.getInt("vfxshadowalpha1")));
//                g2.setPaint(gradient);
//                g2.fillRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")
//                                + block.getInt("dimh")),
//                        eUtils.scaleInt(block.getInt("dimw")),
//                        eUtils.scaleInt((int)(block.getInt("toph")*cVars.getDouble("vfxshadowfactor")))
//                );
//                gradient = new GradientPaint(
//                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
//                                - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("dimh")
//                                + (int)(block.getInt("toph")*cVars.getDouble("vfxshadowfactor"))),
//                        new Color(0,0,0,cVars.getInt("vfxshadowalpha1")),
//                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw")/2
//                                - cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
//                                + block.getInt("dimh")
//                                + (int)(block.getInt("toph")*cVars.getDouble("vfxshadowfactor"))*2),
//                        new Color(0,0,0, cVars.getInt("vfxshadowalpha2")));
//                g2.setPaint(gradient);
//                g2.fillRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
//                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")
//                                + block.getInt("dimh")
//                         + (int)(block.getInt("toph")*cVars.getDouble("vfxshadowfactor"))),
//                        eUtils.scaleInt(block.getInt("dimw")),
//                        eUtils.scaleInt((int)(block.getInt("toph")*cVars.getDouble("vfxshadowfactor")))
//                );
            }
        }
    }
}
