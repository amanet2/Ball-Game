public class gDoableBlockReturn {
    public gBlock getBlock(String[] args) {
        return null;
    }
    public void storeBlock(gBlock blockToLoad, gScene sceneToStore) {
        sceneToStore.getThingMap(blockToLoad.get("type")).put(
                cScripts.createId(),
                blockToLoad
        );
    }
}
