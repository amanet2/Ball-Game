import java.awt.*;

public class dBlockWallsShading {
    public static void drawBlockWallsShadingFlat(Graphics2D g2, gBlock block) {
        g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
        g2.setColor(new Color(0, 0, 0, 255));
        if (sVars.isOne("vfxenableshading")) {
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("wallh"))
                );
                gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        new Color(0, 0, 0, cVars.getInt("vfxwalloutlinealpha1")),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        new Color(0, 0, 0, cVars.getInt("vfxwalloutlinealpha2")));
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

    public static void drawBlockWallsShadingCorner(Graphics2D g2, gBlock block, Polygon pw) {
        g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
        g2.setColor(new Color(0, 0, 0, 255));
        if (sVars.isOne("vfxenableshading")) {
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                        eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                                - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("dimh")),
                        new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                g2.setPaint(gradient);
                g2.drawPolygon(pw);
                g2.fillPolygon(pw);
            }
        }
    }
}
