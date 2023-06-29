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
        dThings.drawBlockWallsShadingFlat(g2, this);
        dThings.drawBlockTopCube(g2, this);
    }
}