import java.util.HashMap;
import java.util.TreeMap;

public class xComStatusServer extends xCom {
    public String doCommand(String fullCommand) {
        HashMap<String, TreeMap<String, String>> sorted = new HashMap<>();
        for (String id : nServer.instance().clientArgsMap.keySet()) {
            sorted.put(id, new TreeMap<>(nServer.instance().clientArgsMap.get(id)));
        }
        return sorted.toString();
    }
}