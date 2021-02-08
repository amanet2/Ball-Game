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
                case gProp.TELEPORTER:
                    eManager.currentMap.propLoadMap.get("PROP_TELEPORTER").putProp(int0, int1, x, y, w, h);
                    break;
                case gProp.SCOREPOINT:
                    eManager.currentMap.propLoadMap.get("PROP_SCOREPOINT").putProp(int0, int1, x, y, w, h);
                    break;
                case gProp.FLAGRED:
                    eManager.currentMap.propLoadMap.get("PROP_FLAGRED").putProp(int0, int1, x, y, w, h);
                    break;
                case gProp.FLAGBLUE:
                    eManager.currentMap.propLoadMap.get("PROP_FLAGBLUE").putProp(int0, int1, x, y, w, h);
                    break;
                case gProp.POWERUP:
                    eManager.currentMap.propLoadMap.get("PROP_POWERUP").putProp(int0, int1, x, y, w, h);
                    break;
                case gProp.BOOSTUP:
                    eManager.currentMap.propLoadMap.get("PROP_BOOSTUP").putProp(int0, int1, x, y, w, h);
                    break;
                case gProp.SPAWNPOINT:
                    eManager.currentMap.propLoadMap.get("PROP_SPAWNPOINT").putProp(int0, int1, x, y, w, h);
                    break;
                default:
                    break;
            }
            return "";
        }
        return "usage: e_putprop <code> <int0> <int1> <x> <y> <w> <h>";
    }
}
