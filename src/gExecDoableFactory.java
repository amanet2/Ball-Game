import java.util.HashMap;

public class gExecDoableFactory {
    HashMap<String, gExecDoable> execDoableMap;
    private static gExecDoableFactory instance = null;

    private gExecDoableFactory() {
        execDoableMap = new HashMap<>();
    }

    public void init() {
        for(String prefabName : eManager.prefabSelection) {
            execDoableMap.put("prefabs/" + prefabName, new gExecDoable("prefabs/" + prefabName));
        }
        for(String fileName : eManager.configSelection) {
            execDoableMap.put("config/" + fileName, new gExecDoable("config/" + fileName));
        }
    }

    public static gExecDoableFactory instance() {
        if(instance == null)
            instance = new gExecDoableFactory();
        return instance;
    }
}
