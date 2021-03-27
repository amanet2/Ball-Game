public class gCollision extends gThing {
    int[] xarr;
    int[] yarr;
    int npoints;
    public gCollision(int[] xa, int[] ya, int n) {
        super();
        put("type", "THING_COLLISION");
        xarr = xa;
        yarr = ya;
        npoints = n;
    }
}
