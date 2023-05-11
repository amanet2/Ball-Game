public class eGameSession extends Thread implements Runnable {
    private final eTimer timer;
    private final eGameLogic gameLogic;
    private long tickTimeNanos;
    private final int tickRate;
    private boolean playing;

    public eGameSession(eGameLogic logic, int rate) {
        gameLogic = logic;
        timer = new eTimer();
        tickRate = rate;
        playing = true;
        ((eGameLogicAdapter) logic).setParentSession(this);
        start();
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

    public void destroy() {
        playing = false;
    }

    private void init(){
        timer.init();
        tickTimeNanos = timer.snapshotTimeNanos();
        gameLogic.init();
    }

    private void cleanup() {
        gameLogic.cleanup();
    }

    private void loop() {
        while (playing) {
            timer.sync();
            input();

            while(behindTimer()) {
                update();
            }
            render();
            sync();
        }
        System.out.println("SESSION LOOP EXITED " + gameLogic.toString());
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
