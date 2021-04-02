public class gDoableItemReturnSpawnPoint extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemSpawnPoint spawnpoint = new gItemSpawnPoint(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return spawnpoint;
    }
    public void storeItem(gItem itemToLoad, gScene sceneToStore) {
        super.storeItem(itemToLoad, sceneToStore);
    }
}
