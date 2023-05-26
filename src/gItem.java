import java.awt.Image;

public class gItem extends gThing {
    public Image sprite;

    public void activateItem(gThing p) {
        String theScript = get("script");
        if(!theScript.equals("null"))
            xMain.shellLogic.console.ex(String.format("%s %s %s", theScript, get("id"), p.get("id")));
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
        put("script", "null");
        put("waypoint", "0");
        put("flare", "null");
        sprite = sp;
    }
}
