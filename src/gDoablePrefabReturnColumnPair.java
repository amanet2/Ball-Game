public class gDoablePrefabReturnColumnPair extends  gDoablePrefabReturn {
    public gPrefabColumnPair getPrefab(String[] args) {
        gPrefabColumnPair prefab = new gPrefabColumnPair(
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
