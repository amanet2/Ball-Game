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

    public nState(String stateMapString) {
        map = new gArgSet();
        stateMapString = stateMapString.trim();
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
