import java.awt.*;
import java.util.HashMap;

public class gPrefabFactory {
    HashMap<String, gPrefab> prefabMap;
    private static gPrefabFactory instance = null;

    private gPrefabFactory() {
        prefabMap = new HashMap<>();

    }

    public void init() {
        //dummy function for intance() to be called
    }

    public static gPrefabFactory instance() {
        if(instance == null)
            instance = new gPrefabFactory();
        return instance;
    }
}
