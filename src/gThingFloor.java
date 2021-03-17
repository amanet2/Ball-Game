import java.awt.*;

public class gThingFloor extends gThing{
    Color colorFloor;

    public gThingFloor(int x, int y, int w, int h, String colorfloor, int brightness) {
        super();
        put("type", "THING_FLOOR");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("brightness", brightness);
        put("colorfloor", colorfloor); //r,g,b,a

        String[] colortoks = get("colorfloor").split(",");
        colorFloor = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
