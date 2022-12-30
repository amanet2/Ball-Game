public class eGameServerLogic implements eGameLogic {
    private int ticks = 0;
    private long tickCounterTime = -1;

    public eGameServerLogic() {

    }

    @Override
    public void init() throws Exception {
        xCon.ex("exec config/itemsdef");
    }

    @Override
    public void input() {

    }

    @Override
    public void update() {
        long gameTimeMillis = gTime.gameTime;

        ticks++;
        if(tickCounterTime < gameTimeMillis) {
            uiInterface.netReportServer = ticks;
            ticks = 0;
            tickCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
