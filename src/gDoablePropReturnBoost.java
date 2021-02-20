public class gDoablePropReturnBoost extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropBoost prop = new gPropBoost(
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
        sceneToStore.getThingMap("PROP_BOOST").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropBoost boost = new gPropBoost(int0, int1, x, y, w, h);
        boost.put("id", cScripts.createId());
        boost.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_BOOST").size());
        boost.putInt("native", 1);
        eManager.currentMap.scene.props().add(boost);
        eManager.currentMap.scene.getThingMap("PROP_BOOST").put(boost.get("id"), boost);
    }
}
