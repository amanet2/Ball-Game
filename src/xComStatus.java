import java.util.HashMap;
import java.util.TreeMap;

public class xComStatus extends xCom {
    public String doCommand(String fullCommand) {
        HashMap<String, TreeMap<String, String>> sorted = new HashMap<>();
        for(String id: nServer.clientArgsMap.keySet()) {
            sorted.put(id, new TreeMap<>(nServer.clientArgsMap.get(id)));
        }
        return sorted.toString();
    }
}