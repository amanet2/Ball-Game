public class gDoableCollisionReturn {
    public gCollision getCollision(String[] args) {
        String[] xarrString = args[0].split("\\.");
        String[] yarrString = args[1].split("\\.");
        int[] xarr = new int[xarrString.length];
        for(int i = 0; i < xarr.length; i++) {
            xarr[i] = Integer.parseInt(xarrString[i]);
        }
        int[] yarr = new int[yarrString.length];
        for(int i = 0; i < yarr.length; i++) {
            yarr[i] = Integer.parseInt(yarrString[i]);
        }
        int npoints = Integer.parseInt(args[2]);
        return new gCollision(xarr, yarr, npoints);
    }

    public void storeCollision(gCollision collisionToLoad, gScene sceneToStore) {
        String collisionId = Integer.toString(eManager.currentMap.scene.collisionIdCtr);
        sceneToStore.getThingMap("THING_COLLISION").put(collisionId, collisionToLoad);
        eManager.currentMap.scene.collisionIdCtr += 1;
    }
}
