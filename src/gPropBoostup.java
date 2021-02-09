public class gPropBoostup extends gProp {
    public void propEffect(gPlayer p) {
        p.putInt("mov0", 1);
        p.putInt("mov1", 0);
        xCon.ex(new String[]{"cv_jumping 1", "cv_inboost 1", "cv_falltime 0"});
    }

    public gPropBoostup(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.BOOSTUP, ux, uy, x, y, w, h);
    }
}
