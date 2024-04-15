import java.awt.*;

public class gBlockCube extends gBlock {
    int wallh; //for cubes
    int toph; //for cubes
    TexturePaint wallTexture;
    GradientPaint shadingOverlay;

    public gBlockCube(String id, String prefabId, int x, int y, int w, int h, int toph, int wallh) {
        super(id, prefabId, x, y, w, h);
        this.type = "BLOCK_CUBE";
        this.toph = toph;
        this.wallh = wallh;
        if(wallh < 300) {
            shadingOverlay = new GradientPaint(
                    coords[0] + dims[0] / 2, coords[1] + toph,
                    gColors.getColorFromName("clrw_walllowshading1"),
                    coords[0] + dims[0] / 2, coords[1] + dims[1],
                    gColors.getColorFromName("clrw_walllowshading2")
            );
        }
        else {
            shadingOverlay = new GradientPaint(
                    coords[0] + dims[0] / 2, coords[1] + toph,
                    gColors.getColorFromName("clrw_wallshading1"),
                    coords[0] + dims[0] / 2, coords[1] + dims[1],
                    gColors.getColorFromName("clrw_wallshading2")
            );
        }
    }
}