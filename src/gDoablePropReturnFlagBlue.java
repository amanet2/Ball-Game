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
        super.storeProp(propToLoad, sceneToStore);
        sceneToStore.getThingMap("PROP_FLAGBLUE").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropFlagBlue flagblue = new gPropFlagBlue(int0, int1, x, y, w, h);
        flagblue.put("id", cScripts.createId());
        flagblue.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_FLAGBLUE").size());
        flagblue.putInt("native", 1);
        eManager.currentMap.scene.props().add(flagblue);
        eManager.currentMap.scene.getThingMap("PROP_FLAGBLUE").put(flagblue.get("id"), flagblue);
    }
}
