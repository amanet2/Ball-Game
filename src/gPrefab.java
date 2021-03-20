import java.util.ArrayList;

public class gPrefab extends gThing {
    ArrayList<gBlock> blocks;

    public gPrefab(int x, int y) {
        super();
        put("type", "THING_PREFAB");
        putInt("coordx", x);
        putInt("coordy", y);
        blocks = new ArrayList<>();
    }
}
