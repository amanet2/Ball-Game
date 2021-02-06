import java.awt.*;

public class dShadows {
    public static void drawTileShadows(Graphics2D g2, gThingTile t) {
        if(sVars.isOne("vfxenableshadows")) {
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            if(t.getInt("dim1h") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                + t.getInt("dim0h") + t.getInt("dim1h")),
                        new Color(0,0,0,cVars.getInt("vfxshadowalpha1")),
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                + t.getInt("dim0h") + t.getInt("dim1h")
                                + (int)(t.getInt("dim1h")*cVars.getDouble("vfxshadowfactor"))),
                        new Color(0,0,0, cVars.getInt("vfxshadowalpha2")));
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy")-cVars.getInt("camy")+t.getInt("dim0h")
                                +t.getInt("dim1h")),
                        eUtils.scaleInt(t.getInt("dim1w")),
                        eUtils.scaleInt((int)(t.getInt("dim1h")*cVars.getDouble("vfxshadowfactor")))
                );
            }
            if(t.getInt("dim4h") > 0) {
                GradientPaint gradient = new GradientPaint(
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                        new Color(0,0,0,cVars.getInt("vfxshadowalpha1")),
                        eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                + (int)(t.getInt("dim4h")*cVars.getDouble("vfxshadowfactor"))),
                        new Color(0,0,0, cVars.getInt("vfxshadowalpha2")));
                g2.setPaint(gradient);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy")-cVars.getInt("camy")+t.getInt("dimh")),
                        eUtils.scaleInt(t.getInt("dim4w")),
                        eUtils.scaleInt((int)(t.getInt("dim4h")*cVars.getDouble("vfxshadowfactor")))
                );
            }
        }
    }
}
