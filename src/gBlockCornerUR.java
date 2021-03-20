import java.awt.*;

public class gBlockCornerUR extends gBlock{
    Color colorWall;

    public gBlockCornerUR(int x, int y, int w, int h, int toph, int wallh, String colortop, String colorwall) {
        super(x, y, w, h, colortop);
        put("type", "BLOCK_CORNERUR");
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
