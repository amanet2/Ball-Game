import java.util.HashMap;

public class gCollisionFactory {
    HashMap<String, gDoableCollisionReturn> collisionLoadMap;
    private static gCollisionFactory instance = null;

    private gCollisionFactory() {
        collisionLoadMap = new HashMap<>();
    }

    public static gCollisionFactory instance() {
        if(instance == null)
            instance = new gCollisionFactory();
        return instance;
    }
}
