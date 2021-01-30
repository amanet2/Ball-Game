public class gDoablePropReturnTeleporter extends gDoablePropReturn {
    public gProp getProp(String[] args) {
        System.out.println("ASDF");
        gPropTeleporter prop = new gPropTeleporter(
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
}
