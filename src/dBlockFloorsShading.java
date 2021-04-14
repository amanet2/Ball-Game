import java.awt.*;

public class dBlockFloorsShading {
    public static void drawBlockFloorShading(Graphics2D g2, gBlockFloor block) {
        g2.setStroke(dFonts.thickStroke);
        g2.setColor(new Color(0, 0, 0, 255));
        if (sVars.isOne("vfxenableshading")) {
//            GradientPaint gradient = new GradientPaint(
//                    sSettings.width/2,0,
//                    new Color(0,0,0, cVars.getInt("vfxflooroutlinealpha1")),
//                    sSettings.width/2, sSettings.height,
//                    new Color(0,0,0,cVars.getInt("vfxroofoutlinealpha2")));
//            GradientPaint gradient2 = new GradientPaint(
//                    sSettings.width/2,0,
//                    new Color(0,0,0, cVars.getInt("vfxfloorshadingalpha1")),
//                    sSettings.width/2, sSettings.height,
//                    new Color(0,0,0,cVars.getInt("vfxfloorshadingalpha2")));
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0, 0, 0, cVars.getInt("vfxfloorshadingalpha1")),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2 - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy") + block.getInt("dimh")),
                    new Color(0, 0, 0, cVars.getInt("vfxfloorshadingalpha2")));
            GradientPaint gradient2 = new GradientPaint(
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0, 0, 0, cVars.getInt("vfxflooroutlinealpha1")),
                    eUtils.scaleInt(block.getInt("coordx") + block.getInt("dimw") / 2
                            - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("dimh")),
                    new Color(0, 0, 0, cVars.getInt("vfxflooroutlinealpha2")));
            g2.setPaint(gradient2);
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("dimh"))
            );
            g2.setStroke(dFonts.thickStroke);
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("dimh")),
                    eUtils.scaleInt(5), eUtils.scaleInt(5)
            );
        }
    }
}
