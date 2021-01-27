public class gPropBallBouncy extends gProp {
    private void bouncePlayerBounds(int velA, int velB, String velP) {
        //bounce away from a player colliding into prop
        //velB and velA come from player and compare Up/Down and Left/Right in pairs
        if(velA > velB)
            putInt(velP, velA + 1);
    }
    public void bounceOffPlayerBounds(gPlayer p) {
        bouncePlayerBounds(p.getInt("vel3"), p.getInt("vel2"), "vel3");
        bouncePlayerBounds(p.getInt("vel2"), p.getInt("vel3"), "vel2");
        bouncePlayerBounds(p.getInt("vel1"), p.getInt("vel0"), "vel1");
        bouncePlayerBounds(p.getInt("vel0"), p.getInt("vel1"), "vel0");
    }
    public void propEffect(gPlayer p) {
        bounceOffPlayerBounds(p);
    }

    public gPropBallBouncy(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.BALLBOUNCY, ux, uy, x, y, w, h);
    }
}
