public class gDoablePropReturnFlagBlue extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropFlagBlue prop = new gPropFlagBlue(
                Integer.valueOf(args[0]),
                Integer.valueOf(args[1]),
                Integer.valueOf(args[2]),
                Integer.valueOf(args[3]),
                Integer.valueOf(args[4]),
                Integer.valueOf(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.flagsblue().add((gPropFlagBlue) propToLoad);
//        sceneToStore.flagsblueMap().put(propToLoad.get("id"), propToLoad);
    }
}
