public class gCollisionFactory {
    gDoableCollisionReturn collisionLoader;
    private static gCollisionFactory instance = null;

    private gCollisionFactory() {
        collisionLoader = new gDoableCollisionReturn();
    }

    public static gCollisionFactory instance() {
        if(instance == null)
            instance = new gCollisionFactory();
        return instance;
    }
}
