public class gDoablePropReturnFlagBlue extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropFlagBlue prop = new gPropFlagBlue(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.getThingMap("PROP_FLAGBLUE").put(propToLoad.get("id"), propToLoad);
    }
}
