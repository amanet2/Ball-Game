import java.util.HashMap;

public class gPrefabFactory {
    HashMap<String, gDoablePrefabReturn> prefabLoadMap;
    private static gPrefabFactory instance = null;

    private gPrefabFactory() {
        prefabLoadMap = new HashMap<>();
    }

    public static gPrefabFactory instance() {
        if(instance == null)
            instance = new gPrefabFactory();
        return instance;
    }
}
