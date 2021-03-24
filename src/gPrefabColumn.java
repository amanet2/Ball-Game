public class gPrefabColumn extends gPrefab {
    public gPrefabColumn(int x, int y) {
        super(x, y);
        put("type", "PREFAB_COLUMN");
        //add blocks to prefab array here, based on x y coords!
        gBlockCornerLL columnL = new gBlockCornerLL(x + 300, y + 300, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255");
//        columnL.put("frontwall", "1");
        blocks.add(columnL);
        gBlockCornerLR columnR = new gBlockCornerLR(x + 750, y + 300, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255");
//        columnR.put("frontwall", "1");
        blocks.add(columnR);
        blocks.add(new gBlockCornerUL(x + 300, y, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255"));
        blocks.add(new gBlockCornerUR(x + 750, y, 300, 1200, 150, 1050,
                "120.120.120.255", "120.120.200.255"));
    }
}
