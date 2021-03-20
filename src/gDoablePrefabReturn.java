public class gDoablePrefabReturn {
    public gPrefab getPrefab(String[] args) {
        return null;
    }

    public void storePrefab(gPrefab prefabToLoad, gScene sceneToStore) {
        int prefabId = eManager.currentMap.scene.objectIdCtrs.get(prefabToLoad.get("type"));
        sceneToStore.getThingMap(prefabToLoad.get("type")).put(
                Integer.toString(prefabId),
                prefabToLoad
        );
        eManager.currentMap.scene.objectIdCtrs.put(prefabToLoad.get("type"), prefabId + 1);
        //iterate through blocks and store THOSE
        for(gBlock block : prefabToLoad.blocks) {
            int blockId = eManager.currentMap.scene.objectIdCtrs.get(block.get("type"));
            sceneToStore.getThingMap(block.get("type")).put(
                    Integer.toString(blockId),
                    block
            );
            eManager.currentMap.scene.objectIdCtrs.put(block.get("type"), blockId + 1);
        }
    }
}
