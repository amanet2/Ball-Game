public class gDoablePropReturnBallBouncy extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropBallBouncy prop = new gPropBallBouncy(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        super.storeProp(propToLoad, sceneToStore);
        sceneToStore.getThingMap("PROP_BALLBOUNCY").put(propToLoad.get("id"), propToLoad);
    }
}
