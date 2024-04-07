public class gItem extends gThing {
    String flare;
    String script;

    public void activateItem(gThing p) {
        if(!script.equals("null"))
            xMain.shellLogic.console.ex(String.format("%s %s %s", script, id, p.id));
    }

    public gItem(String id, String type, int x, int y, int w, int h, String sp, String script, String flare) {
        super();
        this.id = id;
        this.type = type;
        if(type.trim().length() < 1)
            this.type = "null";
        coords[0] = x;
        coords[1] = y;
        dims[0] = w;
        dims[1] = h;
        sprite = sp.equalsIgnoreCase("null") ? null : gTextures.getGScaledImage(eManager.getPath(sp), dims[0], dims[1]);
        this.script = script;
        this.flare = flare;
    }

    public void addToScene(gScene scene) {
        super.addToScene(scene);
        scene.getThingMap("THING_ITEM").put(id, this);
    }
}
