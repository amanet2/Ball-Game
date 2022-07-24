public class eGameEngine implements Runnable {
    private eTimer timer;
    private final eGameLogic gameLogic;
    private boolean playing = true;

    public eGameEngine(eGameLogic logic) {
        gameLogic = logic;
        timer = new eTimer();
    }

    @Override
    public void run() {
        try {
            init();
            loop();
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        finally {
            cleanup();
        }
    }

    private void init() throws Exception {
        oDisplay.instance().showFrame();
        timer.init();
        gameLogic.init();
    }

    private void cleanup() {
        gameLogic.cleanup();
    }

    private void loop() {
        while (playing) {
            timer.sync();

            input();

            while(timer.behind()) {
                timer.update();
                update();
            }
            render();
            sync();
        }
    }

    private void input() {
        gameLogic.input();
    }

    private void update() {
        gameLogic.update();
    }

    private void render() {
        //draw gfx
        oDisplay.instance().frame.repaint();
        gameLogic.render();
    }

    private void sync() {
        // framerate limit
        if(sSettings.framerate > 0) {
            long nextFrameTime = (timer.gameTimeNanos() + (1000000000/sSettings.framerate));
            while (nextFrameTime > timer.currentTimeNanos()) {
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException ignored) {
                }
            }
        }
    }
}
