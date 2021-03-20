public class gDoableBlockReturnCornerLR extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockCornerLR block = new gBlockCornerLR(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                args[6],
                args[7]
        );
        if(args.length > 8)
            block.put("frontwall", args[8]);
        if(args.length > 9)
            block.put("backtop", args[9]);
        return block;
    }

    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        super.storeBlock(blockToLoad, sceneToStore);
        sceneToStore.getThingMap("BLOCK_CORNERLR").put(
                Integer.toString(sceneToStore.getThingMap("BLOCK_CORNERLR").size()),
                blockToLoad
        );
    }
}
