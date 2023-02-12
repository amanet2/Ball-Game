public class eGameClientLogic implements eGameLogic {
    private int ticks = 0;
    private long frameCounterTime = -1;
    private long tickCounterTime = -1;

    public eGameClientLogic() {

    }

    @Override
    public void init() throws Exception {
        oDisplay.instance().showFrame();
        gCamera.init();
        gAnimations.init();
        xCon.ex("exec config/itemsdef");
    }

    @Override
    public void input() {
        gTime.gameTime = System.currentTimeMillis();
        iInput.readKeyInputs();
    }

    @Override
    public void update() {
        long gameTimeMillis = gTime.gameTime;
        if(sSettings.IS_CLIENT) {
//            nClient.instance().netLoop();
            nClient.instance().processPackets();
            cClientLogic.gameLoop(gameTimeMillis);
        }
        ticks += 1;
        if(tickCounterTime < gameTimeMillis) {
            uiInterface.netReportClient = ticks;
            ticks = 0;
            tickCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void render() {
        oDisplay.instance().frame.repaint();
        long gameTimeMillis = gTime.gameTime;
        if (frameCounterTime < gameTimeMillis) {
            uiInterface.fpsReport = uiInterface.frames;
            uiInterface.frames = 0;
            frameCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void cleanup() {

    }
}
