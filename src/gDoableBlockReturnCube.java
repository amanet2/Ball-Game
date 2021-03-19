public class gDoableBlockReturnCube extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockCube block = new gBlockCube(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                args[6],
                args[7]
        );
        return block;
    }

    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        super.storeBlock(blockToLoad, sceneToStore);
        sceneToStore.getThingMap("BLOCK_CUBE").put(
                Integer.toString(sceneToStore.getThingMap("BLOCK_CUBE").size()),
                blockToLoad
        );
    }
}
