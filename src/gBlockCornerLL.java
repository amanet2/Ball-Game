import java.awt.*;

public class gBlockCornerLL extends gBlock{
    Color colorTop;

    public gBlockCornerLL(int x, int y, int w, int h, String colortop, int brightness) {
        super(gBlocks.CORNERLL, x, y, w, h);
        putInt("brightness", brightness);
        put("colortop", colortop); //r,g,b,a

        String[] colortoks = get("colortop").split(",");
        colorTop = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
