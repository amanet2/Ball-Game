public class gPropBoostup extends gProp {
    public void propEffect(gPlayer p) {
//        p.putInt("mov0", 1);
        p.putInt("vel0", cVars.getInt("velocitysuperspeed"));
    }

    public gPropBoostup(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.BOOSTUP, ux, uy, x, y, w, h);
    }
}
