import java.awt.*;

public class gItem extends gThing {
    public Image sprite;

    public void activateItem(gPlayer p) {
        //to be subclassed and execute on intersection
        System.out.println("no activation method");
    }

    public gItem(int x, int y, int w, int h) {
        super();
        put("type", "THING_ITEM");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
    }
}
