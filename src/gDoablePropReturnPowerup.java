public class gDoablePropReturnPowerup extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropPowerup prop = new gPropPowerup(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]));
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.powerups().add((gPropPowerup) propToLoad);
//        sceneToStore.powerupsMap().put(propToLoad.get("id"), propToLoad);
    }
}
