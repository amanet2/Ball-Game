import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class gScheduler {
    protected final ConcurrentHashMap<String, Queue<gDoable>> events;
    protected Queue<gDoable> eventQueue;

    private void dequeueCommands() {
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
    }

    public void executeCommands() {
        dequeueCommands();
        while (eventQueue.size() > 0) {
            gDoable event = eventQueue.peek();
            event.doCommand();
            if(eventQueue.size() > 0)
                eventQueue.remove();
        }
    }

    public void put(String key, gDoable event) {
        if(!events.containsKey(key))
            events.put(key, new LinkedList<>());
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
