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
        super.storeProp(propToLoad, sceneToStore);
        sceneToStore.getThingMap("PROP_POWERUP").put(propToLoad.get("id"), propToLoad);
    }

    public void putProp(int int0, int int1, int x, int y, int w, int h) {
        gPropPowerup powerup = new gPropPowerup(int0, int1, x, y, w, h);
        powerup.put("id", cScripts.createID(8));
        powerup.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_POWERUP").size());
        powerup.putInt("native", 1);
        eManager.currentMap.scene.props().add(powerup);
        eManager.currentMap.scene.getThingMap("PROP_POWERUP").put(powerup.get("id"), powerup);
    }
}
