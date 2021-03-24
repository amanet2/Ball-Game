public class gDoablePrefabReturnColumn extends  gDoablePrefabReturn {
    public gPrefabColumn getPrefab(String[] args) {
        gPrefabColumn prefab = new gPrefabColumn(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return prefab;
    }
    public void storePrefab(gPrefab prefabToLoad, gScene sceneToStore) {
        //iterate through blocks and store THOSE
        super.storePrefab(prefabToLoad, sceneToStore);
    }
}
