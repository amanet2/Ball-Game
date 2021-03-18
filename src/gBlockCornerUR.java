import java.awt.*;

public class gBlockCornerUR extends gBlock{
    Color colorWall;

    public gBlockCornerUR(int x, int y, int w, int h, int toph, int wallh, String colortop, String colorwall,
                          int brightness) {
        super(gBlocks.CORNERUR, x, y, w, h, colortop, brightness);
        putInt("toph", toph);
        putInt("wallh", wallh);
        put("colorwall", colorwall); //r,g,b,a

        String[] colortoks = colorwall.split(",");
        colorWall = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
