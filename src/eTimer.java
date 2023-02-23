public class eTimer {
    private long snapshotTimeNanos;

    public void init() {
        snapshotTimeNanos = currentTimeNanos();
    }

    public void sync() {
        snapshotTimeNanos = currentTimeNanos();
    }

    public long snapshotTimeNanos() {
        return snapshotTimeNanos;
    }

    public long currentTimeNanos() {
        return System.nanoTime();
    }
}
