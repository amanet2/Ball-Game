import java.util.HashMap;

public class gExecDoableFactory {
    HashMap<String, gExecDoable> execDoableMap;
    private static gExecDoableFactory instance = null;

    private gExecDoableFactory() {
        execDoableMap = new HashMap<>();
    }

    public void init() {
        for(String prefabName : eManager.prefabFileSelection) {
            execDoableMap.put("prefabs/" + prefabName, new gExecDoable("prefabs/" + prefabName));
        }
        for(String fileName : eManager.configFileSelection) {
            execDoableMap.put("config/" + fileName, new gExecDoable("config/" + fileName));
        }
        for(String fileName : eManager.scriptFileSelection) {
            execDoableMap.put("scripts/" + fileName, new gExecDoable("scripts/" + fileName));
        }
    }

    public static gExecDoableFactory instance() {
        if(instance == null)
            instance = new gExecDoableFactory();
        return instance;
    }
}
