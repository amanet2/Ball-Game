public class gDoablePropReturnSpawnpoint extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        gPropSpawnpoint prop = new gPropSpawnpoint(
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
        sceneToStore.getThingMap("PROP_SPAWNPOINT").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropSpawnpoint spawnpoint = new gPropSpawnpoint(int0, int1, x, y, w, h);
        spawnpoint.put("id", cScripts.createID(8));
        spawnpoint.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
        spawnpoint.putInt("native", 1);
        eManager.currentMap.scene.props().add(spawnpoint);
        eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").put(spawnpoint.get("id"), spawnpoint);
    }
}
