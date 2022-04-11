import java.util.HashMap;

public class gPrefabFactory {
    HashMap<String, gPrefab> prefabMap;
    private static gPrefabFactory instance = null;

    private gPrefabFactory() {
        prefabMap = new HashMap<>();
    }

    public void init() {

    }

    public void initFromSelection(String[] prefabSelection) {
        for(String prefabName : prefabSelection) {
            prefabMap.put("prefabs/"+prefabName, new gPrefab(String.format("prefabs/%s", prefabName)));
        }
    }

    public static gPrefabFactory instance() {
        if(instance == null)
            instance = new gPrefabFactory();
        return instance;
    }
}
