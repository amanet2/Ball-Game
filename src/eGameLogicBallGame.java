public class eGameLogicBallGame implements eGameLogic {
    private int ticks = 0;
    private long frameCounterTime = -1;
    private long tickCounterTime = -1;

    public eGameLogicBallGame() {

    }

    @Override
    public void init() throws Exception {
        gAnimations.init();
        gItemFactory.init();
    }

    @Override
    public void input() {
        iInput.readKeyInputs();
    }

    @Override
    public void update() {
        long gameTimeMillis = gTime.gameTime;
        if(sSettings.IS_CLIENT) {
            nClient.instance().netLoop();
//            cClientLogic.gameLoop(gameTimeMillis);
        }
        ticks += 1;
        if(tickCounterTime < gameTimeMillis) {
            uiInterface.tickReport = ticks;
            ticks = 0;
            tickCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void render() {
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
