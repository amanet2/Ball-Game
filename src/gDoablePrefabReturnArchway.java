public class gDoablePrefabReturnArchway extends  gDoablePrefabReturn {
    public gPrefabArchway getPrefab(String[] args) {
        gPrefabArchway prefab = new gPrefabArchway(
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
