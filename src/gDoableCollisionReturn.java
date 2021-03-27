public class gDoableCollisionReturn {
    public gBlock getCollision(String[] args) {
        return null;
    }

    public void storeCollision(gCollision collisionToLoad, gScene sceneToStore) {
        String collisionId = Integer.toString(eManager.currentMap.scene.collisionIdCtr);
        sceneToStore.getThingMap("THING_COLLISION").put(collisionId, collisionToLoad);
        sceneToStore.getThingMap(collisionToLoad.get("type")).put(collisionId, collisionToLoad);
        eManager.currentMap.scene.collisionIdCtr += 1;
    }
}
