import java.util.HashMap;
import java.util.TreeMap;

public class xComStatus extends xCom {
    public String doCommand(String fullCommand) {
        HashMap<String, TreeMap<String, String>> sorted = new HashMap<>();
        if(sSettings.IS_CLIENT) {
            for (String id : nClient.instance().serverArgsMap.keySet()) {
                sorted.put(id, new TreeMap<>(nClient.instance().serverArgsMap.get(id)));
            }
        }
        else if(sSettings.IS_SERVER) {
            for (String id : nServer.instance().clientArgsMap.keySet()) {
                sorted.put(id, new TreeMap<>(nServer.instance().clientArgsMap.get(id)));
            }
        }
        return sorted.toString();
    }
}