public class gDoableBlockReturnSquare extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockSquare block = new gBlockSquare(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                args[5],
                args[6],
                Integer.parseInt(args[7])
        );
        return block;
    }

    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        super.storeBlock(blockToLoad, sceneToStore);
        sceneToStore.getThingMap("BLOCK_SQUARE").put(
                Integer.toString(sceneToStore.getThingMap("BLOCK_SQUARE").size()),
                blockToLoad
        );
    }
}
