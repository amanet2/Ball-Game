public class gBlock extends gThing {
    public gBlock(int x, int y, int w, int h) {
        super();
        setCoords(x, y);
        setDims(w, h);
        put("type", "THING_BLOCK");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
    }
}
