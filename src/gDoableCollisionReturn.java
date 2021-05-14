public class gDoableCollisionReturn {
    public gCollision getCollision(String[] args) {
        int npoints = Integer.parseInt(args[2]);
        String[] rawXargs = args[0].split("\\.");
        for(int i = 0; i < rawXargs.length; i++) {
            String rawX = rawXargs[i];
            if(rawX.charAt(0) == '$') {
                int transformedX;
                String[] rxtoksadd = rawX.split("\\+");
                String[] rxtokssub = rawX.split("-");
                if(rxtoksadd.length > 1) {
                    int rxmod0 = sVars.getInt(rxtoksadd[0]);
                    int rxmod1 = Integer.parseInt(rxtoksadd[1]);
                    transformedX = rxmod0+rxmod1;
                }
                else if(rxtokssub.length > 1) {
                    int rxmod0 = sVars.getInt(rxtokssub[0]);
                    int rxmod1 = Integer.parseInt(rxtokssub[1]);
                    transformedX = rxmod0-rxmod1;
                }
                else {
                    transformedX = sVars.getInt(rawX);
                }
                rawXargs[i] = Integer.toString(transformedX);
            }
        }

        String[] rawYargs = args[1].split("\\.");
        for (int i = 0; i < rawYargs.length; i++) {
            String rawY = rawYargs[i];
            if(rawY.charAt(0) == '$') {
                int transformedY;
                String[] rytoksadd = rawY.split("\\+");
                String[] rytokssub = rawY.split("-");
                if(rytoksadd.length > 1) {
                    int rymod0 = sVars.getInt(rytoksadd[0]);
                    int rymod1 = Integer.parseInt(rytoksadd[1]);
                    transformedY = rymod0+rymod1;
                }
                else if(rytokssub.length > 1) {
                    int rymod0 = sVars.getInt(rytokssub[0]);
                    int rymod1 = Integer.parseInt(rytokssub[1]);
                    transformedY = rymod0-rymod1;
                }
                else {
                    transformedY = sVars.getInt(rawY);
                }
                rawYargs[i] = Integer.toString(transformedY);
            }
        }

        int[] xarr = new int[rawXargs.length];
        for(int i = 0; i < xarr.length; i++) {
            xarr[i] = Integer.parseInt(rawXargs[i]);
        }
        int[] yarr = new int[rawYargs.length];
        for(int i = 0; i < yarr.length; i++) {
            yarr[i] = Integer.parseInt(rawYargs[i]);
        }

        return new gCollision(xarr, yarr, npoints);
    }

    public void storeCollision(gCollision collisionToLoad) {
        if(sSettings.IS_SERVER)
            storeCollisionDelegate(collisionToLoad, cServerLogic.scene);
        if(sSettings.IS_CLIENT)
            storeCollisionDelegate(collisionToLoad, cClientLogic.scene);
    }

    private void storeCollisionDelegate(gCollision collisionToLoad, gScene sceneToStore) {
        String collisionId = Integer.toString(sceneToStore.collisionIdCtr);
        sceneToStore.getThingMap("THING_COLLISION").put(collisionId, collisionToLoad);
        sceneToStore.collisionIdCtr += 1;
    }
}
