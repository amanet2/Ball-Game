import java.util.HashMap;

public class gBlockFactory {
    HashMap<String, gDoableBlockReturn> blockLoadMap;
    private static gBlockFactory instance = null;

    private gBlockFactory() {
        blockLoadMap = new HashMap<>();
        blockLoadMap.put("BLOCK_SQUARE", new gDoableBlockReturnSquare());
    }

    public static gBlockFactory instance() {
        if(instance == null)
            instance = new gBlockFactory();
        return instance;
    }
}
