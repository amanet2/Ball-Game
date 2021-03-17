import java.awt.*;

public class gThingBlock extends gThing{
    Color colorTop;
    Color colorWall;

    public gThingBlock(int x, int y, int w, int h, int toph, String colortop,
                       String colorwall, int brightness) {
        super();
        put("type", "THING_BLOCK");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("brightness", brightness);
        putInt("toph", toph); //"wallh" will be dimh - toph
        put("colortop", colortop); //r,g,b,a
        put("colorwall", colorwall); //r,g,b,a

        String[][] colortoks = {get("colortop").split(","), get("colorwall").split(",")};
        colorTop = new Color(Integer.parseInt(colortoks[0][0]), Integer.parseInt(colortoks[0][1]),
                Integer.parseInt(colortoks[0][2]), Integer.parseInt(colortoks[0][3]));
        colorWall = new Color(Integer.parseInt(colortoks[1][0]), Integer.parseInt(colortoks[1][1]),
                Integer.parseInt(colortoks[1][2]), Integer.parseInt(colortoks[1][3]));
    }
}
