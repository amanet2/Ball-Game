import java.awt.*;

public class gBlockCornerLR extends gBlock{
    Color colorWall;

    public gBlockCornerLR(int x, int y, int w, int h, int toph, int wallh, String colortop, String colorwall) {
        super(gBlocks.CORNERLR, x, y, w, h, colortop);
        putInt("toph", toph);
        putInt("wallh", wallh);
        put("colorwall", colorwall); //r,g,b,a
        put("frontwall", "0");
        put("backtop", "0");

        String[] colortoks = colorwall.split(",");
        colorWall = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
