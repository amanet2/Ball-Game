public class gDoablePrefabReturnCageB extends  gDoablePrefabReturn {
    public gPrefabCageB getPrefab(String[] args) {
        gPrefabCageB prefab = new gPrefabCageB(
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
