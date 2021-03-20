public class gDoableBlockReturn {
    public gBlock getBlock(String[] args) {
        return null;
    }
    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        int blockId = eManager.currentMap.scene.objectIdCtrs.get(blockToLoad.get("type"));
        sceneToStore.getThingMap(blockToLoad.get("type")).put(
            Integer.toString(blockId),
            blockToLoad
        );
        eManager.currentMap.scene.objectIdCtrs.put(blockToLoad.get("type"), blockId + 1);
    }
}
