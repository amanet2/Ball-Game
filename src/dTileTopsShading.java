import java.awt.*;

public class dTileTopsShading {
    public static void drawTileTopShadingPre(Graphics2D g2, gTile t) {
        if(t.getInt("dim0h") > 0) {
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0,0,0, cVars.getInt("vfxroofoutlinealpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                    new Color(0,0,0,cVars.getInt("vfxroofoutlinealpha2")));
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(t.getInt("dim0w")),
                    eUtils.scaleInt(t.getInt("dim0h")),
                    eUtils.scaleInt(5),
                    eUtils.scaleInt(5)
            );
            gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                    new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
            g2.setPaint(gradient);
            g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    eUtils.scaleInt(t.getInt("dim0w")),
                    eUtils.scaleInt(t.getInt("dim0h"))
            );
        }
        if(t.getInt("dim3h") > 0) {
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                            + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                    new Color(0,0,0, cVars.getInt("vfxroofoutlinealpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")
                            + t.getInt("dimh") - t.getInt("dim4h")),
                    new Color(0,0,0,cVars.getInt("vfxroofoutlinealpha2")));
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                            - t.getInt("dim3h") - t.getInt("dim4h")),
                    eUtils.scaleInt(t.getInt("dim3w")),
                    eUtils.scaleInt(t.getInt("dim3h")),
                    eUtils.scaleInt(5),
                    eUtils.scaleInt(5)
            );
            gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                            + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                    new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")
                            + t.getInt("dimh") - t.getInt("dim4h")),
                    new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
            g2.setPaint(gradient);
            g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                            - t.getInt("dim3h") - t.getInt("dim4h")),
                    eUtils.scaleInt(t.getInt("dim3w")),
                    eUtils.scaleInt(t.getInt("dim3h"))
            );
        }
    }

    public static void drawTileTopShadingPost(Graphics2D g2, gTile t) {
        if(t.getInt("dim5w") > 0) {
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha2")));
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                    eUtils.scaleInt(t.getInt("dim5w")),
                    eUtils.scaleInt(t.getInt("dim5h")),
                    eUtils.scaleInt(5),
                    eUtils.scaleInt(5)
            );
            gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha2")));
            g2.setPaint(gradient);
            g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                    eUtils.scaleInt(t.getInt("dim5w")),
                    eUtils.scaleInt(t.getInt("dim5h"))
            );
        }
        if(t.getInt("dim6w") > 0) {
            GradientPaint gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha2")));
            g2.setPaint(gradient);
            g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                            + t.getInt("dimw") - t.getInt("dim6w")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                    eUtils.scaleInt(t.getInt("dim6w")),
                    eUtils.scaleInt(t.getInt("dim6h")),
                    eUtils.scaleInt(5),
                    eUtils.scaleInt(5)
            );
            gradient = new GradientPaint(
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha1")),
                    eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                    new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha2")));
            g2.setPaint(gradient);
            g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")
                            - t.getInt("dim6w")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                    eUtils.scaleInt(t.getInt("dim6w")),
                    eUtils.scaleInt(t.getInt("dim6h"))
            );
        }
    }
}
