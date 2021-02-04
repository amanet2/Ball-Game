public class xComEditorPutFlare extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            int w = Integer.parseInt(toks[3]);
            int h = Integer.parseInt(toks[4]);
            int propx = eUtils.roundToNearest(
                eUtils.unscaleInt(Integer.parseInt(toks[1]))+cVars.getInt("camx")-w/2,
                cEditorLogic.state.snapToX);
            int propy = eUtils.roundToNearest(
                eUtils.unscaleInt(Integer.parseInt(toks[2]))+cVars.getInt("camy")-h/2,
                cEditorLogic.state.snapToY);
            gFlare p = new gFlare(
                propx,
                propy,
                w,
                h,
                Integer.parseInt(toks[5]),
                Integer.parseInt(toks[6]),
                Integer.parseInt(toks[7]),
                Integer.parseInt(toks[8]),
                Integer.parseInt(toks[9]),
                Integer.parseInt(toks[10]),
                Integer.parseInt(toks[11]),
                Integer.parseInt(toks[12])
            );
            eManager.currentMap.scene.flares().add(p);
            return "";
        }
        return "usage: e_putflare <x> <y> <r1> <g1> <b1> <a1> <r2> <g2> <b2> <a2>";
    }
}
