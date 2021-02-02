public class xComEditorPutProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            int propcode = Integer.parseInt(toks[1]);
            int int0 = Integer.parseInt(toks[2]);
            int int1 = Integer.parseInt(toks[3]);
            int x = Integer.parseInt(toks[4]);
            int y = Integer.parseInt(toks[5]);
            int w = Integer.parseInt(toks[6]);
            int h = Integer.parseInt(toks[7]);
            switch(propcode) {
//                TELEPORTER = 0;
//                LADDER = 2;
//                SCOREPOINT = 3;
//                FLAGRED = 5;
//                FLAGBLUE = 6;
//                POWERUP = 7;
//                BOOSTUP = 8;
//                BALLBOUNCY = 9;
                case gProp.TELEPORTER:
                    gPropTeleporter teleporter = new gPropTeleporter(int0, int1, x, y, w, h);
                    teleporter.put("id", cScripts.createID(8));
                    teleporter.putInt("tag",
                            eManager.currentMap.scene.getThingMap("PROP_TELEPORTER").size());
                    teleporter.putInt("native", 1);
                    eManager.currentMap.scene.props().add(teleporter);
                    eManager.currentMap.scene.getThingMap("PROP_TELEPORTER").put(teleporter.get("id"),
                            teleporter);
                    break;
                case gProp.POWERUP:
                    gPropPowerup powerup = new gPropPowerup(int0, int1, x, y, w, h);
                    powerup.putInt("tag", eManager.currentMap.scene.getThingMap("PROP_POWERUP").size());
                    powerup.putInt("native", 1);
                    eManager.currentMap.scene.props().add(powerup);
                    eManager.currentMap.scene.getThingMap("PROP_POWERUP").put(cScripts.createID(8), powerup);
                    break;
                default:
                    break;
            }

//            gProp p = new gProp(
//                Integer.parseInt(toks[1]),
//                Integer.parseInt(toks[2]),
//                Integer.parseInt(toks[3]),
//                Integer.parseInt(toks[4]),
//                Integer.parseInt(toks[5]),
//                Integer.parseInt(toks[6]),
//                Integer.parseInt(toks[7])
//            );
//            p.putInt("tag", eManager.currentMap.scene.props().size());
//            eManager.currentMap.scene.props().add(p);

            return "";
        }
        return "usage: e_putprop <code> <int0> <int1> <x> <y> <w> <h>";
    }
}
