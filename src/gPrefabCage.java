public class gPrefabCage extends gPrefab {
    public gPrefabCage(int x, int y) {
        super(x, y);
        put("type", "PREFAB_CAGE");
        //add blocks to prefab array here, based on x y coords!
        blocks.add(new gBlockCube(x, y, 1200, 1200, 150, 1050, "120,120,120,255",
                "120,120,200,255"));
        blocks.add(new gBlockCube(x + 1200, y, 1200, 1200, 150, 1050, "120,120,120,255",
                "120,120,200,255"));
        blocks.add(new gBlockCube(x + 2400, y, 1200, 1200, 150, 1050, "120,120,120,255",
                "120,120,200,255"));
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
//        blocks.add(new gBlockCube());
    }
//DONE    putblock BLOCK_CUBE 0 -600 1200 1200 150 1050 120,120,120,255 120,120,200,255
//    putblock BLOCK_CUBE -1200 -600 1200 1200 150 1050 120,120,120,255 120,120,200,255
//    putblock BLOCK_CUBE 1200 -600 1200 1200 150 1050 120,120,120,255 120,120,200,255
//    putblock BLOCK_CUBE -300 -600 300 1800 1800 0 120,120,120,255 120,120,200,255
//    putblock BLOCK_CUBE 1200 -600 300 1800 1800 0 120,120,120,255 120,120,200,255
//    putblock BLOCK_CUBE 0 1200 1200 1200 150 0 120,120,120,255 120,120,200,255
//    putblock BLOCK_CORNERUR -300 1200 300 1200 150 1050 120,120,120,255 120,120,200,255 1
//    putblock BLOCK_CORNERUL 1200 1200 300 1200 150 1050 120,120,120,255 120,120,200,255 1
//    putblock BLOCK_FLOOR 0 600 1200 1200 100,100,60,255
//    putblock BLOCK_FLOOR 0 1800 1200 1200 100,100,60,255
//    putblock BLOCK_FLOOR 1200 600 1200 1200 100,100,60,255
//    putblock BLOCK_FLOOR 1200 1800 1200 1200 100,100,60,255
//    putblock BLOCK_FLOOR -1200 600 1200 1200 100,100,60,255
//    putblock BLOCK_FLOOR -1200 1800 1200 1200 100,100,60,255
}
