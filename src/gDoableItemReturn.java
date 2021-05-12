public class gDoableItemReturn {
    public gItem getItem(String[] args) {
        return null;
    }
    public void storeItem(gItem itemToLoad) {
        if(sSettings.IS_SERVER) {
            gScene sceneToStore = cServerLogic.scene;
            int itemId = sceneToStore.itemIdCtr;
            itemToLoad.putInt("id", itemId);
            itemToLoad.putInt("itemid", itemId);
            sceneToStore.getThingMap("THING_ITEM").put(Integer.toString(itemId), itemToLoad);
            sceneToStore.getThingMap(itemToLoad.get("type")).put(Integer.toString(itemId), itemToLoad);
            sceneToStore.itemIdCtr += 1;
        }
        if(sSettings.IS_CLIENT) {
            gScene sceneToStore = cClientLogic.scene;
            int itemId = sceneToStore.itemIdCtr;
            itemToLoad.putInt("id", itemId);
            itemToLoad.putInt("itemid", itemId);
            sceneToStore.getThingMap("THING_ITEM").put(Integer.toString(itemId), itemToLoad);
            sceneToStore.getThingMap(itemToLoad.get("type")).put(Integer.toString(itemId), itemToLoad);
            sceneToStore.itemIdCtr += 1;
        }
    }
}
