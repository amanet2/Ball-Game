import java.awt.*;

public class dBlockWallsShading {
    public static void drawBlockWallsShadingFlat(Graphics2D g2, gBlock block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient;
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("toph")),
                            gColors.getWorldColorFromName("walllowshading1"),
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh")),
                            gColors.getWorldColorFromName("walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("toph")),
                            gColors.getWorldColorFromName("wallshading1"),
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh")),
                            gColors.getWorldColorFromName("wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("wallh"))
                );
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("toph")),
                            gColors.getWorldColorFromName("walllowoutline1"),
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh")),
                            gColors.getWorldColorFromName("walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("toph")),
                            gColors.getWorldColorFromName("walloutline1"),
                            eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                    + block.getInt("dimh")),
                            gColors.getWorldColorFromName("walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("wallh")),
                        eUtils.scaleInt(5), eUtils.scaleInt(5)
                );
            }
        }
    }
}
