public class gPropLadder extends gProp {
    public void propEffect(gPlayer p) {
        cVars.put("onladder", "1");
    }

    public gPropLadder(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.LADDER, ux, uy, x, y, w, h);
    }
}
