public class gDoablePrefabReturnBigBox extends  gDoablePrefabReturn {
    public gPrefabBigBox getPrefab(String[] args) {
        gPrefabBigBox prefab = new gPrefabBigBox(
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
