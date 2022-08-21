import java.util.Collection;
import java.util.HashMap;

public class nStateMap {
    private final HashMap<String, nState> map;

    public void put(String k, nState v) {
        map.put(k, v);
    }

    public nState get(String k) {
        return map.get(k);
    }

    public boolean contains(String k) {
        return map.containsKey(k);
    }

    public void remove(String k) {
        map.remove(k);
    }

    public Collection<String> keys() {
        return map.keySet();
    }

    public String toString() {
        return map.toString();
    }

    public nStateMap() {
        map = new HashMap<>();
    }
}
