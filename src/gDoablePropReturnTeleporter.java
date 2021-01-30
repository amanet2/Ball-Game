public class gDoablePropReturnTeleporter extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropTeleporter prop = new gPropTeleporter(
                Integer.valueOf(args[0]),
                Integer.valueOf(args[1]),
                Integer.valueOf(args[2]),
                Integer.valueOf(args[3]),
                Integer.valueOf(args[4]),
                Integer.valueOf(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.teleporters().add((gPropTeleporter) propToLoad);
        sceneToStore.teleportersMap().put(propToLoad.get("id"), propToLoad);
    }
}
