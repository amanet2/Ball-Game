public class eTimer {
    private long snapshotTimeMillis;
    private long snapshotTimeNanos;

    public void init() {
        snapshotTimeMillis = currentTimeMillis();
        snapshotTimeNanos = currentTimeNanos();
    }

    public void sync() {
        snapshotTimeMillis = currentTimeMillis();
        snapshotTimeNanos = currentTimeNanos();
    }

    public long snapshotTimeMillis() {
        return snapshotTimeMillis;
    }

    public long snapshotTimeNanos() {
        return snapshotTimeNanos;
    }

    public long currentTimeNanos() {
        return System.nanoTime();
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
