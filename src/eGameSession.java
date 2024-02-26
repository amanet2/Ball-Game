public class eGameSession extends Thread implements Runnable {
    private final eGameLogic gameLogic;
    int tickRate;
    private boolean playing;

    public eGameSession(eGameLogic logic, int rate) {
        gameLogic = logic;
        tickRate = rate;
        playing = true;
        ((eGameLogicAdapter) logic).setParentSession(this);
        start();
    }

    @Override
    public void run() {
        try {
            long snapshotTimeNanos = System.nanoTime();
            long tickTimeNanos = snapshotTimeNanos;
            gameLogic.init();
            while (playing) {
                snapshotTimeNanos = System.nanoTime();
                gameLogic.input();
                while(tickTimeNanos < snapshotTimeNanos) {
                    tickTimeNanos += (1000000000/tickRate);
                    gameLogic.update();
                }
                gameLogic.render();
                if(tickRate > 0) {
                    long nextFrameTime = (snapshotTimeNanos + (1000000000/tickRate));
                    while (nextFrameTime > System.nanoTime()) {
                        try {
                            sleep(1);
                        }
                        catch (InterruptedException ignored) {
                        }
                    }
                }
            }
            System.out.println("SESSION LOOP EXITED " + gameLogic);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        finally {
            destroy();
        }
    }

    public void destroy() {
        playing = false;
        gameLogic.cleanup(); // if this is commented out server doesnt cleanly exit
    }
}
