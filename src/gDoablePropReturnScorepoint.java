public class gDoablePropReturnScorepoint extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropScorepoint prop = new gPropScorepoint(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.scorepoints().add((gPropScorepoint) propToLoad);
//        sceneToStore.scorepointsMap().put(propToLoad.get("id"), propToLoad);
    }
}
