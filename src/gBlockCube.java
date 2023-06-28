import java.awt.*;

public class gBlockCube extends gBlock {
    int wallh; //for cubes
    int toph; //for cubes

    public gBlockCube(String id, String prefabId, int x, int y, int w, int h, int toph, int wallh) {
        super(id, prefabId, "BLOCK_CUBE", x, y, w, h);
        System.out.println(toph);
        this.toph = toph;
        this.wallh = wallh;
    }

    public void draw(Graphics2D g2) {
        dThings.drawShadowBlockFlat(g2, this);
        g2.setPaint(xMain.shellLogic.wallTexture);
        g2.fillRect(coords[0], coords[1] + toph, dims[0], wallh);
        dThings.drawBlockWallsShadingFlat(g2, this);
        dThings.drawBlockTopCube(g2, this);
    }
}
