import java.util.HashMap;

public class gExecDoableFactory {
    HashMap<String, gExecDoable> execDoableMap;
    private static gExecDoableFactory instance = null;

    private gExecDoableFactory() {
        execDoableMap = new HashMap<>();
    }

    public void init() {
        sSettings.prefab_titles = new String[eManager.prefabFileSelection.length];
        int c = 0;
        for(String prefabName : eManager.prefabFileSelection) {
            execDoableMap.put("prefabs/" + prefabName, new gExecDoable("prefabs/" + prefabName));
            sSettings.prefab_titles[c++] = prefabName;
        }
        for(String fileName : eManager.configFileSelection) {
            execDoableMap.put("config/" + fileName, new gExecDoable("config/" + fileName));
        }
        for(String fileName : eManager.itemFilesSelection) {
            execDoableMap.put("items/" + fileName, new gExecDoable("items/" + fileName));
        }
        for(String fileName : eManager.scriptFilesSelection) {
            execDoableMap.put("scripts/" + fileName, new gExecDoable("scripts/" + fileName));
        }
    }

    public static gExecDoableFactory instance() {
        if(instance == null)
            instance = new gExecDoableFactory();
        return instance;
    }
}
