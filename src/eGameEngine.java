public class eGameEngine implements Runnable {
    private static eGameEngine instance;
    private eTimer timer;
    private final eGameLogic gameLogic;

    public static eGameEngine getInstance() {
        if(instance == null) {
            instance = new eGameEngine(new eGameLogicBallGame());
        }
        return instance;
    }


    private eGameEngine(eGameLogic logic) {
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
        while (true) {
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
//            while (nextFrameTime > System.nanoTime()) {
            while (nextFrameTime > timer.gameTimeNanos()) {
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException ignored) {
                }
            }
        }
    }
}
