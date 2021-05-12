public class gDoableBlockReturn {
    public gBlock getBlock(String[] args) {
        return null;
    }
    public void storeBlock(gBlock blockToLoad) {
        if(sSettings.IS_SERVER) {
            gScene sceneToStore = cServerLogic.scene;
            int blockId = sceneToStore.blockIdCtr;
            sceneToStore.getThingMap("THING_BLOCK").put(Integer.toString(blockId), blockToLoad);
            sceneToStore.getThingMap(blockToLoad.get("type")).put(Integer.toString(blockId), blockToLoad);
            sceneToStore.blockIdCtr += 1;
        }
        if(sSettings.IS_CLIENT) {
            gScene sceneToStore = cClientLogic.scene;
            int blockId = sceneToStore.blockIdCtr;
            sceneToStore.getThingMap("THING_BLOCK").put(Integer.toString(blockId), blockToLoad);
            sceneToStore.getThingMap(blockToLoad.get("type")).put(Integer.toString(blockId), blockToLoad);
            sceneToStore.blockIdCtr += 1;
        }
    }
}
