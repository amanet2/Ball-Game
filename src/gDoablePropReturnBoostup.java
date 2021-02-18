public class gDoablePropReturnBoostup extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropBoostup prop = new gPropBoostup(
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
        sceneToStore.getThingMap("PROP_BOOSTUP").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropBoostup boostup = new gPropBoostup(int0, int1, x, y, w, h);
        boostup.put("id", cScripts.createId());
        boostup.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_BOOSTUP").size());
        boostup.putInt("native", 1);
        eManager.currentMap.scene.props().add(boostup);
        eManager.currentMap.scene.getThingMap("PROP_BOOSTUP").put(boostup.get("id"), boostup);
    }
}
