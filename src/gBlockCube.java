public class gBlockCube extends gBlock {
    int wallh; //for cubes
    int toph; //for cubes

    public gBlockCube(String id, String prefabId, int x, int y, int w, int h, int toph, int wallh) {
        super(id, prefabId, x, y, w, h);
        this.type = "BLOCK_CUBE";
        this.toph = toph;
        this.wallh = wallh;
    }
}