public class gPropSafepoint extends gProp {
    public void propEffect(gPlayer p) {
        if(getInt("int0") > 0) {
            cVars.put("survivesafezone", "1");
        }
    }

    public gPropSafepoint(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.SAFEPOINT, ux, uy, x, y, w, h);
    }
}
