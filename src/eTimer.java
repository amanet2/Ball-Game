public class eTimer {
    private long gameTimeMillis;
    private long gameTimeNanos;
    private long tickTimeNanos;

    public void init() {
        gameTimeMillis = System.currentTimeMillis();
        gameTimeNanos = System.nanoTime();
        tickTimeNanos = gameTimeNanos;
    }

    public void sync() {
        gameTimeMillis = System.currentTimeMillis();
        gameTimeNanos = System.nanoTime();
        gTime.gameTime = gameTimeMillis;
        gTime.gameTimeNanos = gameTimeNanos;
    }

    public void update() {
        tickTimeNanos += (1000000000/sSettings.rategame);
    }

    public boolean behind() {
        return tickTimeNanos < gameTimeNanos;
    }

    public long gameTimeNanos() {
        return gameTimeNanos;
    }

    public long gameTimeMillis() {
        return gameTimeMillis;
    }
}
