import java.awt.*;

public class gBlockCornerUL extends gBlock{
    Color colorWall;

    public gBlockCornerUL(int x, int y, int w, int h, int toph, String colortop, String colorwall, int brightness) {
        super(gBlocks.CORNERUL, x, y, w, h, colortop, brightness);
        putInt("toph", toph); //"wallh" will be dimh - toph
        put("colorwall", colorwall); //r,g,b,a

        String[] colortoks = colorwall.split(",");
        colorWall = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
