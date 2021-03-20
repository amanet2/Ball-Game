import java.awt.*;

public class gPrefab extends gThing {
    gBlock[] blocks;

    public gPrefab load(String[] args) {
        return null;
    }

    public gPrefab(int x, int y, gBlock[] blocksarray) {
        super();
        put("type", "THING_PREFAB");
        putInt("coordx", x);
        putInt("coordy", y);
        blocks = blocksarray;
    }
}
