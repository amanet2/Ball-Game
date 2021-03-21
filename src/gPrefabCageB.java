public class gPrefabCageB extends gPrefab {
    public gPrefabCageB(int x, int y) {
        super(x, y);
        put("type", "PREFAB_CAGEB");
        //add blocks to prefab array here, based on x y coords!
        blocks.add(new gBlockCornerLL(x + 2400, y, 300, 150, 150, 0, "120.120.120.255",
                "120.120.200.255"));
        blocks.add(new gBlockCornerLR(x + 900, y, 300, 150, 150, 0, "120.120.120.255",
                "120.120.200.255"));
        blocks.add(new gBlockCube(x + 900, y + 150, 300, 1650, 1650, 0,
                "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCube(x + 2400, y + 150, 300, 1650, 1650, 0,
                "120.120.120.255", "120.120.200.255"));
        gBlockCube archtop = new gBlockCube(x + 1200, y, 1200, 1200, 150, 225,
                "120.120.120.255", "120.120.200.255");
        archtop.put("frontwall", "1");
        blocks.add(archtop);
        gBlockCube archbottom = new gBlockCube(x + 1200, y + 1800, 1200, 1200, 150, 225,
                "120.120.120.255", "120.120.200.255");
        archbottom.put("frontwall", "1");
        blocks.add(archbottom);
        gBlockCornerUR columnL = new gBlockCornerUR(x + 900, y + 1800, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255");
        columnL.put("frontwall", "1");
        blocks.add(columnL);
        gBlockCornerUL columnR = new gBlockCornerUL(x + 2400, y + 1800, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255");
        columnR.put("frontwall", "1");
        blocks.add(columnR);
        blocks.add(new gBlockFloor(x, y + 1200, 1200, 1200, "100.100.60.255"));
        blocks.add(new gBlockFloor(x + 1200, y + 1200, 1200, 1200, "100.100.60.255"));
        blocks.add(new gBlockFloor(x + 2400, y + 1200, 1200, 1200, "100.100.60.255"));
        blocks.add(new gBlockFloor(x, y + 2400, 1200, 1200, "100.100.60.255"));
        blocks.add(new gBlockFloor(x + 1200, y + 2400, 1200, 1200, "100.100.60.255"));
        blocks.add(new gBlockFloor(x + 2400, y + 2400, 1200, 1200, "100.100.60.255"));
    }
}
