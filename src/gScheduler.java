import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class gScheduler {
    protected final ConcurrentHashMap<String, Queue<gDoable>> events;
    protected ConcurrentLinkedQueue<gDoable> eventQueue;

    public synchronized void executeCommands() {
        long gtime = sSettings.gameTime;
        ArrayList<String> toRemoveIds = new ArrayList<>();
        for (String timestampkey : events.keySet()) {
            if (Long.parseLong(timestampkey) > gtime)
                continue;
            eventQueue.addAll(events.get(timestampkey));
            toRemoveIds.add(timestampkey);
        }
        for(String timeStampKey : toRemoveIds) {
            events.remove(timeStampKey);
        }
        while (eventQueue.size() > 0) { //TODO: crash here when queue.remove() is null
            gDoable o = eventQueue.remove();
            if(o != null)
                o.doCommand();
        }
    }

    public synchronized void put(String key, gDoable event) {
        events.putIfAbsent(key, new LinkedList<>());
        events.get(key).add(event);
    }

    public synchronized void clear() {
        events.clear();
        eventQueue.clear();
    }

    public gScheduler() {
        events = new ConcurrentHashMap<>();
        eventQueue = new ConcurrentLinkedQueue<>();
    }
}
