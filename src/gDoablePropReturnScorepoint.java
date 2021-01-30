public class gDoablePropReturnScorepoint extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropScorepoint prop = new gPropScorepoint(
                Integer.valueOf(args[0]),
                Integer.valueOf(args[1]),
                Integer.valueOf(args[2]),
                Integer.valueOf(args[3]),
                Integer.valueOf(args[4]),
                Integer.valueOf(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.scorepoints().add((gPropScorepoint) propToLoad);
//        sceneToStore.scorepointsMap().put(propToLoad.get("id"), propToLoad);
    }
}
