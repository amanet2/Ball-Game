public class gDoablePropReturnBoostup extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropBoostup prop = new gPropBoostup(
                Integer.valueOf(args[0]),
                Integer.valueOf(args[1]),
                Integer.valueOf(args[2]),
                Integer.valueOf(args[3]),
                Integer.valueOf(args[4]),
                Integer.valueOf(args[5]));
//        prop.putInt("tag", eManager.currentMap.scene.teleporters().size());
//        prop.putInt("native", 1);
//        eManager.currentMap.scene.props().add(prop);
//        eManager.currentMap.scene.teleporters().add(prop);
        return prop;
    }

    public void storeProp(gProp propToLoad, gScene sceneToStore) {
        sceneToStore.boostups().add((gPropBoostup) propToLoad);
//        sceneToStore.boostupsMap().put(propToLoad.get("id"), propToLoad);
    }
}
