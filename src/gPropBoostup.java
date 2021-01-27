public class gPropBoostup extends gProp {
    public void propEffect(gPlayer p) {
        xCon.ex(new String[]{"THING_PLAYER.0.mov0 1", "THING_PLAYER.0.mov1 0", "cv_jumping 1", "cv_inboost 1",
                "cv_falltime 0"});
    }

    public gPropBoostup(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.BOOSTUP, ux, uy, x, y, w, h);
    }
}
