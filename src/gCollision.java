public class gCollision extends gThing {
    int[] xarr;
    int[] yarr;

    public gCollision(int[] xa, int[] ya) {
        super();
        put("type", "THING_COLLISION");
        xarr = xa;
        yarr = ya;
    }
}
