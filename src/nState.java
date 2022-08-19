import java.util.Collection;
import java.util.HashMap;

public class nState {
    protected HashMap<String, String> stateMap;

    public void put(String s, String v) {
        stateMap.put(s, v);
    }

    public String get(String k) {
        return stateMap.get(k);
    }

    public Collection<String> keys() {
        return stateMap.keySet();
    }

    public HashMap<String, String> getDelta(nState oState) {
        //the dummy state is the first state passed to delta
        //server will receive data from client and create...
        //...the state which will call newState.delta(prevSavedState)
        HashMap<String, String> deltaMap = new HashMap<>();
        for(String k : oState.keys()) {
            String tv = oState.get(k);
            if(!get(k).equals(tv)) {
                deltaMap.put(k, tv);
            }
        }
        return deltaMap;
    }

    public nState() {
        stateMap = new HashMap<>();
    }
}
