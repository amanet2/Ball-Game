public class gBlock extends gThing {
    public gBlock load(String[] args) {
        return null;
    }

    public gBlock(int t, int x, int y, int w, int h) {
        super();
        put("type", "THING_BLOCK");
        putInt("code", t);
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
    }
}
