import java.awt.*;

public class gBlockFloor extends gBlock{
    Color colorFloor;

    public gBlockFloor(int x, int y, int w, int h, String colorfloor, int brightness) {
        super(gBlocks.FLOOR, x, y, w, h);
        putInt("brightness", brightness);
        put("colorfloor", colorfloor); //r,g,b,a

        String[] colortoks = get("colorfloor").split(",");
        colorFloor = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
