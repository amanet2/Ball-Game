import java.awt.*;

public class gBlock extends gThing {
    Color colorBase;

    public gBlock(int x, int y, int w, int h, String color) {
        super();
        put("type", "THING_BLOCK");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        put("color", color);

        String[] colortoks = get("color").split("\\.");
        colorBase = new Color(Integer.parseInt(colortoks[0]), Integer.parseInt(colortoks[1]),
                Integer.parseInt(colortoks[2]), Integer.parseInt(colortoks[3]));
    }
}
