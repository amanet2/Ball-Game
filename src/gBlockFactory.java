import java.util.HashMap;

public class gBlockFactory {
    HashMap<String, gDoableBlockReturn> blockLoadMap;
    private static gBlockFactory instance = null;

    private gBlockFactory() {
        blockLoadMap = new HashMap<>();
        blockLoadMap.put("BLOCK_CUBE", new gDoableBlockReturnSquare());
        blockLoadMap.put("BLOCK_FLOOR", new gDoableBlockReturnFloor());
        blockLoadMap.put("BLOCK_CORNERUR", new gDoableBlockReturnCornerUR());
        blockLoadMap.put("BLOCK_CORNERUL", new gDoableBlockReturnCornerUL());
    }

    public static gBlockFactory instance() {
        if(instance == null)
            instance = new gBlockFactory();
        return instance;
    }
}
