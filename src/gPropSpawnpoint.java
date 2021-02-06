public class gPropSpawnpoint extends gProp {
    public gProp load(String[] args) {
        gPropSpawnpoint prop = new gPropSpawnpoint(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                Integer.parseInt(args[6]));
        prop.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
        prop.putInt("native", 1);
        prop.putInt("occupied", 0);
        return prop;
    }
    public void propEffect(gPlayer p) {
        putInt("occupied", 1);
    }

    public gPropSpawnpoint(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.SPAWNPOINT, ux, uy, x, y, w, h);
    }
}
