public class gPrefabColumnPair extends gPrefab {
    public gPrefabColumnPair(int x, int y) {
        super(x, y);
        put("type", "PREFAB_COLUMNPAIR");
        //add blocks to prefab array here, based on x y coords!
        blocks.add(new gBlockFloor(x, y, 1800, 1200, "100,100,60,255"));
        blocks.add(new gBlockFloor(x, y + 1200, 1800, 1200, "100,100,60,255"));
        gBlockCube columnL = new gBlockCube(x + 300, y, 300, 1200, 150, 1050,
                "120,120,120,255", "120,120,200,255");
        columnL.put("frontwall", "1");
        blocks.add(columnL);
        gBlockCube columnR = new gBlockCube(x + 1200, y, 300, 1200, 150, 1050,
                "120,120,120,255", "120,120,200,255");
        columnR.put("frontwall", "1");
        blocks.add(columnR);
//        gBlockCornerLL columnL = new gBlockCornerLL(x + 300, y, 300, 1200, 300, 900,
//                "120,120,120,255", "120,120,200,255");
//        columnL.put("frontwall", "1");
//        blocks.add(columnL);
//        gBlockCornerLR columnR = new gBlockCornerLR(x + 1200, y, 300, 1200, 300, 900,
//                "120,120,120,255", "120,120,200,255");
//        columnR.put("frontwall", "1");
//        blocks.add(columnR);
    }
}
