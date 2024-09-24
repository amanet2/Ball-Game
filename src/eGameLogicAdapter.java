public class eGameLogicAdapter implements eGameLogic {
    private int ticks;
    private long nextsecondnanos;
    protected int tickReport;
    protected eGameSession parentSession;

    public eGameLogicAdapter() {
        ticks = 0;
        nextsecondnanos = 0;
        tickReport = 0;
    }

    @Override
    public void init(){
//        System.out.println(this + "_init " + System.currentTimeMillis());
    }

    @Override
    public void input() {
//        System.out.println(this + "_input " + System.currentTimeMillis());
    }

    @Override
    public void render() {
//        System.out.println(this + "_render " + System.currentTimeMillis());
    }

    @Override
    public void update() {
        ticks++;
        long theTime = System.nanoTime();
        if(nextsecondnanos < theTime) {
            nextsecondnanos = theTime + 1000000000;
            tickReport = ticks;
            ticks = 0;
        }
//        System.out.println(this + "_update " + System.currentTimeMillis());
    }

    @Override
    public void disconnect() {
//        System.out.println(this + "_disconnect " + System.currentTimeMillis());
        parentSession.destroy();
    }

    @Override
    public void cleanup() {
        tickReport = 0;
    }
}
