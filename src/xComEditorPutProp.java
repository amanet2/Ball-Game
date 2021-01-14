public class xComEditorPutProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gProp p = new gProp(
                Integer.parseInt(toks[1]),
                Integer.parseInt(toks[2]),
                Integer.parseInt(toks[3]),
                Integer.parseInt(toks[4]),
                Integer.parseInt(toks[5]),
                Integer.parseInt(toks[6]),
                Integer.parseInt(toks[7])
            );
            p.putInt("tag", eManager.currentMap.scene.props().size());
            eManager.currentMap.scene.props().add(p);
            return "";
        }
        return "usage: e_putprop <code> <int0> <int1> <x> <y> <w> <h>";
    }
}
