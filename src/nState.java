import java.util.Collection;

public class nState {
    protected gArgSet map;

    public void put(String s, String v) {
        map.put(s, v);
    }

    public String get(String k) {
        return map.get(k);
    }

    public boolean contains(String k) {
        return map.contains(k);
    }

    public Collection<String> keys() {
        return map.args.keySet();
    }

    public nState getDelta(nState oState) {
//        System.out.println(this);
//        System.out.println(oState);
//        System.out.println("---");
        //the dummy state is the first state passed to delta
        //server will receive data from client and create...
        //...the state which will call newState.delta(prevSavedState)
        nState deltaState = new nState();
        for(String k : keys()) {
            String tv = get(k);
            System.out.println("GET KEY " + k);
            if(!oState.get(k).equals(tv)) {
                deltaState.put(k, tv);
            }
        }
        return deltaState;
    }

    public nState(String stateMapString) {
        //create return hashmap of key/value pairs
        map = new gArgSet();
        //get rid of the surrounding {} brackets.  this is fake/1-D parsing
        String argstr = stateMapString.substring(1, stateMapString.length()-1);
        for(String pair : argstr.split(",")) {
            String[] vals = pair.split("=");
            map.put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
        }
    }

    public String toString() {
        return map.toString();
    }

    public nState() {
        map = new gArgSet();
    }
}
