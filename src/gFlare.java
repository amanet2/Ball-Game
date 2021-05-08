public class gFlare extends gThing {

    public gFlare(int x, int y, int w, int h, int r1, int g1, int b1, int a1, int r2, int g2, int b2, int a2){
        super();
        put("type", "THING_FLARE");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("r1", r1);
        putInt("g1", g1);
        putInt("b1", b1);
        putInt("a1", a1);
        putInt("r2", r2);
        putInt("g2", g2);
        putInt("b2", b2);
        putInt("a2", a2);
        putInt("mode", 1);
    }
}
