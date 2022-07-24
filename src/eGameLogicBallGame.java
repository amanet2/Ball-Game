public class eGameLogicBallGame implements eGameLogic {
    private int ticks = 0;
    public eGameLogicBallGame() {

    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void input() {
        iInput.readKeyInputs();
    }

    @Override
    public void update() {
        if(sSettings.IS_SERVER)
            cServerLogic.gameLoop();
        if(sSettings.IS_CLIENT)
            cClientLogic.gameLoop();
            uiInterface.camReport[0] = gCamera.getX();
            uiInterface.camReport[1] = gCamera.getY();
            ticks += 1;
            if(uiInterface.tickCounterTime < gTime.gameTime) {
                uiInterface.tickReport = ticks;
                ticks = 0;
                uiInterface.tickCounterTime = gTime.gameTime + 1000;
            }
    }

    @Override
    public void render() {
        long lastFrameTime = gTime.gameTime;
        if (gTime.framecounterTime < lastFrameTime) {
            uiInterface.fpsReport = uiInterface.frames;
            uiInterface.frames = 0;
            gTime.framecounterTime = lastFrameTime + 1000;
        }
    }

    @Override
    public void cleanup() {

    }
}
