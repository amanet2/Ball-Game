public class gDoablePropReturnTeleporter extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropTeleporter prop = new gPropTeleporter(
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
        sceneToStore.getThingMap("PROP_TELEPORTER").put(propToLoad.get("id"), propToLoad);
    }
}
