public class gDoableItemReturn {
    public gItem getItem(String[] args) {
        return null;
    }
    public void storeItem(gItem itemToLoad, gScene sceneToStore) {
        int itemId = eManager.currentMap.scene.itemIdCtr;
        sceneToStore.getThingMap("THING_ITEM").put(Integer.toString(itemId), itemToLoad);
        sceneToStore.getThingMap(itemToLoad.get("type")).put(Integer.toString(itemId), itemToLoad);
        eManager.currentMap.scene.itemIdCtr += 1;
    }
}
