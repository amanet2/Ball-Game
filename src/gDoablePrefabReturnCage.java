public class gDoablePrefabReturnCage extends  gDoablePrefabReturn {
    public gPrefabCage getPrefab(String[] args) {
        gPrefabCage prefab = new gPrefabCage(
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
