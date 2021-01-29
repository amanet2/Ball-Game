public class xComEditorPutProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            int propcode = Integer.parseInt(toks[1]);
            int int0 = Integer.valueOf(toks[2]);
            int int1 = Integer.valueOf(toks[3]);
            int x = Integer.valueOf(toks[4]);
            int y = Integer.valueOf(toks[5]);
            int w = Integer.valueOf(toks[6]);
            int h = Integer.valueOf(toks[7]);
            switch(propcode) {
                case gProp.POWERUP:
                    gPropPowerup prop = new gPropPowerup(int0, int1, x, y, w, h);
                    prop.putInt("tag", eManager.currentMap.scene.powerups().size());
                    prop.putInt("native", 1);
                    eManager.currentMap.scene.props().add(prop);
                    eManager.currentMap.scene.powerups().add(prop);
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
