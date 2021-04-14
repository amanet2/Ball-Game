public class gDoableBlockReturn {
    public gBlock getBlock(String[] args) {
        return null;
    }
    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        int blockId = eManager.currentMap.scene.blockIdCtr;
        sceneToStore.getThingMap("THING_BLOCK").put(Integer.toString(blockId), blockToLoad);
        sceneToStore.getThingMap(blockToLoad.get("type")).put(Integer.toString(blockId), blockToLoad);
        eManager.currentMap.scene.blockIdCtr += 1;
    }
}
