public class gPrefabArchway extends gPrefab {
    public gPrefabArchway(int x, int y) {
        super(x, y);
        put("type", "PREFAB_ARCHWAY");
        //add blocks to prefab array here, based on x y coords!
//        blocks.add(new gBlockFloor(x, y, 1200, 1200, "100.100.60.255"));
        gBlockCornerLR columnL = new gBlockCornerLR(x, y, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255");
        columnL.put("frontwall", "1");
        blocks.add(columnL);
        gBlockCornerLL columnR = new gBlockCornerLL(x + 900, y, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255");
        columnR.put("frontwall", "1");
        blocks.add(columnR);
        blocks.add(new gBlockCube(x + 300, y, 600, 1200, 150, 0,
                "120.120.120.255", "120.120.200.255"));
    }
}
