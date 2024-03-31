import java.math.BigDecimal;

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
            long nextFrameTimeNanos = snapshotTimeNanos + (1000000000/tickRate);
            long sleepForMillis = 0; //millis to sleep
            int sleepForNanos = 0; //nano time remainder to sleep
            gameLogic.init();
            while (playing) {
                snapshotTimeNanos = System.nanoTime();
                nextFrameTimeNanos = snapshotTimeNanos + (1000000000/(long)tickRate);
                gameLogic.input();
                while(tickTimeNanos < snapshotTimeNanos) {
                    tickTimeNanos += (1000000000/tickRate);
                    gameLogic.update();
                }
                gameLogic.render();
                double sleepFor = (double)(nextFrameTimeNanos - System.nanoTime())/1000000.0;
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(sleepFor));
                sleepForMillis = bigDecimal.intValue();
                sleepForNanos = Integer.parseInt(bigDecimal.subtract(new BigDecimal(sleepForMillis)).toPlainString().split("\\.")[1]);
                if(sSettings.powerSave) {
                    if(sleepForMillis > 0 || sleepForNanos > 0) { // inaccurate but gentle on cpu
                        try {
                            sleep(Math.max(0, sleepForMillis), Math.max(0, sleepForNanos));
                        }
                        catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
                else if(tickRate > 0) {
                    while (nextFrameTimeNanos > System.nanoTime()) {
                        //do nothing
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
