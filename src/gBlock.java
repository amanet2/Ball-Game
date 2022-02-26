//import java.awt.*;

public class gBlock extends gThing {
//    public Image sprite;

    public gBlock(int x, int y, int w, int h) {
        super();
        put("type", "THING_BLOCK");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
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
    }
}
