import java.awt.Image;

public class gItem extends gThing {
    public Image sprite;

    public void activateItem(gThing p) {
        if(contains("script")) {
            xCon.ex("setvar itemactid " + p.get("id"));
            xCon.ex("setvar itemrcvid " + get("id"));
            //call script with $1 and $2 as the item and player id
            String theScript = get("script");
            if(theScript.startsWith("exec_new"))
                xCon.ex(String.format("%s %s %s", theScript, get("id"), p.get("id")));
            else
                xCon.ex(theScript);
        }
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
