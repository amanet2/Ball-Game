import java.awt.*;

public class dBlockBrightness {
    public static void drawBlockBrightness(Graphics2D g2, gScene scene) {
//        if (cVars.isOne("flashlight")) {
//            int maxd = 900;
//            int aimerx = eUtils.unscaleInt(cScripts.getMouseCoordinates()[0]);
//            int aimery = eUtils.unscaleInt(cScripts.getMouseCoordinates()[1]);
//            int cx = eUtils.unscaleInt(cVars.getInt("camx"));
//            int cy = eUtils.unscaleInt(cVars.getInt("camy"));
//            int snapX = aimerx + cx;
//            int snapY = aimery + cy;
//            snapX -= eUtils.unscaleInt(cVars.getInt("camx"));
//            snapY -= eUtils.unscaleInt(cVars.getInt("camy"));
//            snapX = eUtils.scaleInt(snapX);
//            snapY = eUtils.scaleInt(snapY);
//            for (gBlock t : scene.blocks()) {
//                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY),
//                        eUtils.scaleInt(maxd / 2), new float[]{0f, 1f},
//                        new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 255 - t.getInt("brightness"))}
//                );
//                g2.setPaint(df);
//                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh")));
//            }
//        }
//        else {
//            for (gBlock t : scene.blocks()) {
//                g2.setColor(new Color(0, 0, 0, 255 - t.getInt("brightness")));
//                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh"))
//                );
//            }
//        }
    }
}
