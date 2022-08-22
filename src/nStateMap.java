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

    public nStateMap getDelta(nStateMap oStateMap) {
        nStateMap deltaStateMap = new nStateMap();
//        System.out.println(oStateMap);
        return deltaStateMap;
    }

    public String toString() {
        return map.toString();
    }

    public nStateMap() {
        map = new HashMap<>();
    }

    public nStateMap(String mapString) {
        map = new HashMap<>();
        if(mapString == null)
            return;
//        System.out.println(mapString);
        String l1String = mapString.substring(1,mapString.length()-1); //get rid of outer '{}'
        String[] playerLoads = l1String.split("}, ");
        for(int i = 0; i < playerLoads.length; i++) {
            String[] idArgs = playerLoads[i].split("=\\{");
            String id = idArgs[0];
            map.put(id, new nState());
            String playerLoad;
            if(i == playerLoads.length-1) //special case of last id in map
                playerLoad = idArgs[1].substring(0, idArgs[1].length() - 1);
            else
                playerLoad = idArgs[1];
//            System.out.println(id);
//            System.out.println(playerLoad);
            for(String pair : playerLoad.split(",")) {
                String[] kvs = pair.split("=");
                map.get(id).put(kvs[0].trim(), kvs.length > 1 ? kvs[1].trim() : "");
            }
        }
        System.out.println(this);
    }
}
