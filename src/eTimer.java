public class eTimer {
    private long gameTimeMillis;
    private long gameTimeNanos;
    private long tickTimeNanos;

    public void init() {
        gameTimeMillis = currentTimeMillis();
        gameTimeNanos = currentTimeNanos();
        tickTimeNanos = gameTimeNanos;
    }

    public void sync() {
        gameTimeMillis = currentTimeMillis();
        gameTimeNanos = currentTimeNanos();
        gTime.gameTime = gameTimeMillis;
    }

    public void update() {
        tickTimeNanos += (1000000000/sSettings.rategame);
    }

    public boolean behind() {
        return tickTimeNanos < gameTimeNanos;
    }

    public long gameTimeMillis() {
        return gameTimeMillis;
    }

    public long gameTimeNanos() {
        return gameTimeNanos;
    }

    public long currentTimeNanos() {
        return System.nanoTime();
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
