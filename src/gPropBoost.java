public class gPropBoost extends gProp {
    public void propEffect(gPlayer p) {
        p.putInt("vel"+getInt("int0"), getInt("int1"));
    }

    public gPropBoost(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.BOOST, ux, uy, x, y, w, h);
    }
}
