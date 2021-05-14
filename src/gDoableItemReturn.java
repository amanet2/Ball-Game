public class gDoableItemReturn {
    public gItem getItem(String[] args) {
        return null;
    }
    public void storeItem(gItem itemToLoad) {
        if(sSettings.IS_SERVER)
            storeItemDelegate(itemToLoad, cServerLogic.scene);
        if(sSettings.IS_CLIENT)
            storeItemDelegate(itemToLoad, cClientLogic.scene);
    }

    private void storeItemDelegate(gItem itemToLoad, gScene sceneToStore) {
        int itemId = sceneToStore.itemIdCtr;
        itemToLoad.putInt("id", itemId);
        itemToLoad.putInt("itemid", itemId);
        sceneToStore.getThingMap("THING_ITEM").put(Integer.toString(itemId), itemToLoad);
        sceneToStore.getThingMap(itemToLoad.get("type")).put(Integer.toString(itemId), itemToLoad);
        sceneToStore.itemIdCtr += 1;
    }
}
