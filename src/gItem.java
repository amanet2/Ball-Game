import java.awt.*;

public class gItem extends gThing {
    public Image sprite;

    public void activateItem(gPlayer p) {
        //to be subclassed and execute on intersection
        if(contains("script"))
            xCon.ex(get("script"));
        xCon.ex("setvar itemactid " + p.get("id"));
    }

    public String saveString() {
        return String.format("putitem %s %s %s %s", get("type"), get("id"), get("coordx"), get("coordy"));
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
