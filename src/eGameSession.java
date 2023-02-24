public class eGameSession extends Thread implements Runnable {
    private final eTimer timer;
    private final eGameLogic gameLogic;
    private long tickTimeNanos;
    private int tickRate;

    public eGameSession(eGameLogic logic, int rate) {
        gameLogic = logic;
        timer = new eTimer();
        tickRate = rate;
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
        timer.init();
        gameLogic.init();
        tickTimeNanos = timer.snapshotTimeNanos();
    }

    private void cleanup() {
        gameLogic.cleanup();
    }

    private void loop() {
        boolean playing = true;
        while (playing) {
            timer.sync();
            input();

            while(behindTimer()) {
                update();
            }
            render();
            sync();
        }
    }

    private boolean behindTimer() {
        return tickTimeNanos < timer.snapshotTimeNanos();
    }

    private void input() {
        gameLogic.input();
    }

    private void update() {
        tickTimeNanos += (1000000000/tickRate);
        gameLogic.update();
    }

    private void render() {
        gameLogic.render();
    }

    private void sync() {
        // framerate limit
        if(tickRate > 0) {
            long nextFrameTime = (timer.snapshotTimeNanos() + (1000000000/tickRate));
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
