public class xComPutProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 7) {
            String propString = toks[1];
            int int0 = Integer.parseInt(toks[2]);
            int int1 = Integer.parseInt(toks[3]);
            int x = Integer.parseInt(toks[4]);
            int y = Integer.parseInt(toks[5]);
            int w = Integer.parseInt(toks[6]);
            int h = Integer.parseInt(toks[7]);
            if(gThingFactory.instance().propLoadMap.containsKey(propString))
                gThingFactory.instance().propLoadMap.get(propString).putProp(int0, int1, x, y, w, h);
            return "";
        }
        return "usage: putprop <PROP_STRING> <int0> <int1> <x> <y> <w> <h>";
    }
}
