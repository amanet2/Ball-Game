public class xComEditorPutProp extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            int propx = eUtils.roundToNearest(eUtils.unscaleInt(Integer.parseInt(toks[2]))
                +cVars.getInt("camx")-cEditorLogic.state.newProp.getInt("dimw")/2, cEditorLogic.state.snapToX);
            int propy = eUtils.roundToNearest(eUtils.unscaleInt(Integer.parseInt(toks[3]))
                +cVars.getInt("camy")-cEditorLogic.state.newProp.getInt("dimh")/2, cEditorLogic.state.snapToY);
            int propw = Integer.parseInt(toks[4]);
            int proph = Integer.parseInt(toks[5]);
            gProp p = new gProp(
                Integer.parseInt(toks[1]),
                cEditorLogic.state.newProp.getInt("int0"),
                cEditorLogic.state.newProp.getInt("int1"),
                propx,
                propy,
                propw,
                proph);
            p.putInt("tag", eManager.currentMap.scene.props().size());
            eManager.currentMap.scene.props().add(p);
            return "";
        }
        return "usage: e_putprop <c> <x> <y> <w> <h>";
    }
}
