import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class gTimeEventSet {
    protected HashMap<String, Queue<gTimeEvent>> events;
    protected Queue<gTimeEvent> eventQueue;

    private void dequeueCommands() {
        long gtime = gTime.gameTime;
        ArrayList<String> toRemoveIds = new ArrayList<>();
        for(String timestampkey : events.keySet()) {
            if(Long.parseLong(timestampkey) > gtime)
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
            gTimeEvent event = eventQueue.peek();
            event.doCommand();
            eventQueue.remove();
        }
    }

    public void put(String key, gTimeEvent event) {
        if(!events.containsKey(key))
            events.put(key, new LinkedList<>());
        events.get(key).add(event);
    }

    public void clear() {
        events.clear();
        eventQueue.clear();
    }

    public gTimeEventSet() {
        events = new HashMap<>();
        eventQueue = new LinkedList<>();
    }
}
