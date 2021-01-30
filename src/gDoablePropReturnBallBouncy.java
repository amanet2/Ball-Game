public class gDoablePropReturnBallBouncy extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropBallBouncy prop = new gPropBallBouncy(
                Integer.valueOf(args[0]),
                Integer.valueOf(args[1]),
                Integer.valueOf(args[2]),
                Integer.valueOf(args[3]),
                Integer.valueOf(args[4]),
                Integer.valueOf(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.ballbouncys().add((gPropBallBouncy) propToLoad);
//        sceneToStore.ballbouncysMap().put(propToLoad.get("id"), propToLoad);
    }
}
