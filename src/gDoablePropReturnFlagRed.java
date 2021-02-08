public class gDoablePropReturnFlagRed extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropFlagRed prop = new gPropFlagRed(
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
        sceneToStore.getThingMap("PROP_FLAGRED").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropFlagRed flagred = new gPropFlagRed(int0, int1, x, y, w, h);
        flagred.put("id", cScripts.createID(8));
        flagred.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_FLAGRED").size());
        flagred.putInt("native", 1);
        eManager.currentMap.scene.props().add(flagred);
        eManager.currentMap.scene.getThingMap("PROP_FLAGRED").put(flagred.get("id"), flagred);
    }
}
