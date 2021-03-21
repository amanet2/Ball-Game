public class gPrefabBigBox extends gPrefab {
    public gPrefabBigBox(int x, int y) {
        super(x, y);
        put("type", "PREFAB_BIGBOX");
        //add blocks to prefab array here, based on x y coords!
        blocks.add(new gBlockCube(x, y, 1200, 1200, 150, 1050, "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCube(x + 1200, y, 1200, 1200, 150, 1050, "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCube(x, y + 1200, 1200, 1200, 150, 1050, "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCube(x + 1200, y + 1200, 1200, 1200, 150, 1050, "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCube(x, y, 300, 1350, 1350, 0, "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCube(x + 2100, y, 300, 1350, 1350, 0, "120.120.120.255", "120.120.200.255"));
//        putblock BLOCK_CUBE -900 3000 1200 1200 150 1050 120.120.120.255 120.120.200.255
//        putblock BLOCK_CUBE 300 3000 1200 1200 150 1050 120.120.120.255 120.120.200.255
//        putblock BLOCK_CUBE -900 4200 1200 1200 150 1050 120.120.120.255 120.120.200.255
//        putblock BLOCK_CUBE 300 4200 1200 1200 150 1050 120.120.120.255 120.120.200.255
//        putblock BLOCK_CUBE -900 3000 300 1350 1350 0 120.120.120.255 120.120.200.255
//        putblock BLOCK_CUBE 1200 3000 300 1350 1350 0 120.120.120.255 120.120.200.255
    }
}
