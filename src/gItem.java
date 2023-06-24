import java.awt.Image;

public class gItem extends gThing {
    public Image sprite;
    String script;

    public void activateItem(gThing p) {
        if(!script.equals("null"))
            xMain.shellLogic.console.ex(String.format("%s %s %s", script, id, p.id));
    }

    public gItem(String type, int x, int y, int w, int h, Image sp) {
        super();
        this.type = type;
        if(type.trim().length() < 1)
            this.type = "null";
        script = "null";
        coords[0] = x;
        coords[1] = y;
        dims[0] = w;
        dims[1] = h;
        sprite = sp;
    }
}
