public class gDoableBlockReturn {
    public gBlock getBlock(String[] args) {
        return null;
    }
    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        int blockId = sceneToStore.blockIdCtr;
        sceneToStore.getThingMap("THING_BLOCK").put(Integer.toString(blockId), blockToLoad);
        sceneToStore.getThingMap(blockToLoad.get("type")).put(Integer.toString(blockId), blockToLoad);
        sceneToStore.blockIdCtr += 1;
    }
}
