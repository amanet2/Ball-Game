public class gBlockCollision extends gBlock {
    public gBlockCollision(String id, String prefabId, int x, int y, int w, int h) {
        super(id, prefabId, x, y, w, h);
        this.type = "BLOCK_COLLISION";
    }
}