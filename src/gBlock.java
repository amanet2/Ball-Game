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

    public void rotateNinetyDegrees() {
        int[] coords = {getInt("coordx"),
                        getInt("coordy")};
        int[][] rot = {{0, -1},
                       {1,  0}};
        int[] result = new int[]{
                rot[0][0] * coords[0] + rot[0][1] * coords[1],
                rot[1][0] * coords[0] + rot[1][1] * coords[1]
        };
        System.out.println("foo");
    }
}
