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

    public nState(String stateMapString) {
        //create return hashmap of key/value pairs
        stateMap = new HashMap<>();
        //get rid of the surrounding {} brackets.  this is fake/1-D parsing
        String argstr = stateMapString.substring(1, stateMapString.length()-1);
        for(String pair : argstr.split(",")) {
            String[] vals = pair.split("=");
            stateMap.put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
        }
    }

    public nState() {
        stateMap = new HashMap<>();
    }
}
