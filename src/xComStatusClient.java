import java.util.HashMap;
import java.util.TreeMap;

public class xComStatusClient extends xCom {
    public String doCommand(String fullCommand) {
        HashMap<String, TreeMap<String, String>> sorted = new HashMap<>();
        for (String id : nClient.instance().serverArgsMap.keySet()) {
            sorted.put(id, new TreeMap<>(nClient.instance().serverArgsMap.get(id)));
        }
        return sorted.toString();
    }
}