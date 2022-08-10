import java.awt.*;

public class gItem extends gThing {
    public Image sprite;

    public void activateItem(gPlayer p) {
        //to be subclassed and execute on intersection
        if(contains("script")) {
            xCon.ex("setvar itemactid " + p.get("id"));
            xCon.ex("setvar itemrcvid " + get("id"));
            xCon.ex(get("script"));
        }
    }

    public String saveString() {
        return String.format("putitem %s %s %s %s", get("type"), get("id"), get("coordx"), get("coordy"));
    }

    public gItem(String type, int x, int y, int w, int h, Image sp) {
        super();
        if(type.trim().length() < 1)
            type = "null";
        put("type", type);
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        sprite = sp;
    }
}
