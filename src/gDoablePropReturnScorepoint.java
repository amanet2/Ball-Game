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
        super.storeProp(propToLoad, sceneToStore);
        propToLoad.putInt("tag", sceneToStore.getThingMap("PROP_SCOREPOINT").size());
        sceneToStore.getThingMap("PROP_SCOREPOINT").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropScorepoint scorepoint = new gPropScorepoint(int0, int1, x, y, w, h);
        scorepoint.put("id", cScripts.createID(8));
        scorepoint.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT").size());
        scorepoint.putInt("native", 1);
        eManager.currentMap.scene.props().add(scorepoint);
        eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT").put(scorepoint.get("id"), scorepoint);
    }
}
