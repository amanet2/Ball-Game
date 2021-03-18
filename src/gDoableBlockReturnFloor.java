public class gDoableBlockReturnFloor extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockFloor block = new gBlockFloor(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                args[4],
                Integer.parseInt(args[5])
        );
        return block;
    }

    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        super.storeBlock(blockToLoad, sceneToStore);
        sceneToStore.getThingMap("BLOCK_FLOOR").put(
                Integer.toString(sceneToStore.getThingMap("BLOCK_FLOOR").size()),
                blockToLoad
        );
    }
}
