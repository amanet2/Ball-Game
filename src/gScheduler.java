import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class gScheduler {
    protected final ConcurrentHashMap<String, Queue<gDoable>> events;
    protected Queue<gDoable> eventQueue;

    public void executeCommands() {
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
        while (eventQueue.size() > 0) {
            eventQueue.remove().doCommand();
        }
    }

    public void put(String key, gDoable event) {
        events.putIfAbsent(key, new LinkedList<>());
        events.get(key).add(event);
    }

    public void clear() {
        events.clear();
        eventQueue.clear();
    }

    public gScheduler() {
        events = new ConcurrentHashMap<>();
        eventQueue = new LinkedList<>();
    }
}
