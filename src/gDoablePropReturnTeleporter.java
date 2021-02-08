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

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropTeleporter teleporter = new gPropTeleporter(int0, int1, x, y, w, h);
        teleporter.put("id", cScripts.createID(8));
        teleporter.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_TELEPORTER").size());
        teleporter.putInt("native", 1);
        eManager.currentMap.scene.props().add(teleporter);
        eManager.currentMap.scene.getThingMap("PROP_TELEPORTER").put(teleporter.get("id"), teleporter);

    }
}
