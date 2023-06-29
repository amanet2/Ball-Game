import java.awt.*;

public class gBlockCube extends gBlock {
    TexturePaint wallTexture;
    TexturePaint topTexture;
    int wallh; //for cubes
    int toph; //for cubes

    public gBlockCube(String id, String prefabId, int x, int y, int w, int h, int toph, int wallh) {
        super(id, prefabId, x, y, w, h);
        this.type = "BLOCK_CUBE";
        this.toph = toph;
        this.wallh = wallh;
        this.wallTexture = xMain.shellLogic.wallTexture;
        this.topTexture = xMain.shellLogic.topTexture;
    }

    public void draw(Graphics2D g2) {
        //floor shadow
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    coords[0] + dims[0]/2,coords[1] + dims[1], gColors.getColorFromName("clrw_shadow1"),
                    coords[0] + dims[0]/2, coords[1] + dims[1] + (int)((wallh)*sSettings.vfxshadowfactor),
                    gColors.getColorFromName("clrw_shadow2")
            );
            g2.setPaint(gradient);
            g2.fillRect(
                    coords[0], coords[1] + dims[1], dims[0], (int)(wallh*sSettings.vfxshadowfactor)
            );
        }
        g2.setPaint(wallTexture);
        g2.fillRect(coords[0], coords[1] + toph, dims[0], wallh);
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (wallh > 0) {
                GradientPaint gradient;
                if(wallh < 300) {
                    gradient = new GradientPaint(
                            coords[0] + dims[0] / 2, coords[1] + toph,
                            gColors.getColorFromName("clrw_walllowshading1"),
                            coords[0] + dims[0] / 2, coords[1] + dims[1],
                            gColors.getColorFromName("clrw_walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            coords[0] + dims[0] / 2, coords[1] + toph,
                            gColors.getColorFromName("clrw_wallshading1"),
                            coords[0] + dims[0] / 2, coords[1] + dims[1],
                            gColors.getColorFromName("clrw_wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(coords[0], coords[1] + toph, dims[0], wallh);
                if(wallh < 300) {
                    gradient = new GradientPaint(
                            coords[0] + dims[0] / 2, coords[1] + toph,
                            gColors.getColorFromName("clrw_walllowoutline1"),
                            coords[0] + dims[0] / 2, coords[1] + dims[1],
                            gColors.getColorFromName("clrw_walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            coords[0] + dims[0] / 2, coords[1] + toph,
                            gColors.getColorFromName("clrw_walloutline1"),
                            coords[0] + dims[0] / 2, coords[1] + dims[1],
                            gColors.getColorFromName("clrw_walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(coords[0], coords[1] + toph, dims[0], wallh, 5, 5);
            }
        }
        g2.setPaint(xMain.shellLogic.topTexture);
        g2.fillRect(coords[0], coords[1], dims[0], toph);
        dFonts.setFontColor(g2, "clrw_topcolor");
        if(wallh > 0 && wallh < 300)
            dFonts.setFontColor(g2, "clrw_topcolordark");
        g2.fillRect(coords[0], coords[1], dims[0], toph);
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    coords[0] + dims[0] / 2, coords[1], gColors.getColorFromName("clrw_roofoutline1"),
                    coords[0] + dims[0] / 2, coords[1] + toph, gColors.getColorFromName("clrw_roofoutline2")
            );
            g2.setPaint(gradient);
            if(wallh > 0 && wallh < 300)
                g2.fillRect(coords[0], coords[1], dims[0], toph);
            g2.drawRoundRect(coords[0], coords[1], dims[0], toph, 5, 5);
        }
    }
}