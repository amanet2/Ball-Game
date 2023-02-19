public class eGameServerLogic implements eGameLogic {
    private int ticks = 0;
    private long nextsecondnanos = 0;

    public eGameServerLogic() {

    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void input() {

    }

    @Override
    public void update() {
//        cServerLogic.gameLoop(System.currentTimeMillis()); // using this makes teleporters broken proly caus client
        long gameTime = gTime.gameTime;
//        nServer.instance().processPackets(gameTime);
//        nServer.instance().checkForUnhandledQuitters();
        cServerLogic.gameLoop(gameTime);
        ticks++;
        long theTime = System.nanoTime();
        if(nextsecondnanos < theTime) {
            nextsecondnanos = theTime + 1000000000;
            uiInterface.tickReportServer = ticks;
            ticks = 0;
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
