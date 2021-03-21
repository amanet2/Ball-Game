import java.util.HashMap;

public class gPrefabFactory {
    HashMap<String, gDoablePrefabReturn> prefabLoadMap;
    private static gPrefabFactory instance = null;

    private gPrefabFactory() {
        prefabLoadMap = new HashMap<>();
        prefabLoadMap.put("PREFAB_CAGE", new gDoablePrefabReturnCage());
        prefabLoadMap.put("PREFAB_CAGEB", new gDoablePrefabReturnCageB());
        prefabLoadMap.put("PREFAB_COLUMNPAIR", new gDoablePrefabReturnColumnPair());
        prefabLoadMap.put("PREFAB_ARCHWAY", new gDoablePrefabReturnArchway());
    }

    public static gPrefabFactory instance() {
        if(instance == null)
            instance = new gPrefabFactory();
        return instance;
    }
}
