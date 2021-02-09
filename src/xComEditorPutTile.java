public class xComEditorPutTile extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 14) {
            int x = Integer.parseInt(toks[1]);
            int y = Integer.parseInt(toks[2]);
            int w = Integer.parseInt(toks[3]);
            int h = Integer.parseInt(toks[4]);
            int nth = Integer.parseInt(toks[5]);
            int nmh = Integer.parseInt(toks[6]);
            int mh = Integer.parseInt(toks[7]);
            int sth = Integer.parseInt(toks[8]);
            int smh = Integer.parseInt(toks[9]);
            int lh = Integer.parseInt(toks[10]);
            int rh = Integer.parseInt(toks[11]);
            String tt = toks[12];
            String mt = toks[13];
            String bt = toks[14];
            int bl = Integer.parseInt(toks[15]);
            gTile t = new gTile(eUtils.roundToNearest(
                eUtils.unscaleInt(x) + cVars.getInt("camx") - w / 2, cEditorLogic.state.snapToX),
                eUtils.roundToNearest(eUtils.unscaleInt(y) + cVars.getInt("camy") - h / 2,
                    cEditorLogic.state.snapToY), w, h, nth, nmh, mh, sth, smh, lh, rh, eUtils.getPath(tt),
                eUtils.getPath(mt), eUtils.getPath(bt), bl);
            t.putInt("id", eManager.currentMap.scene.tiles().size());
            eManager.currentMap.scene.tiles().add(t);
            xCon.ex(String.format("HIDDEN e_selecttile %d",
                eManager.currentMap.scene.tiles().size() - 1));
            return "";
        }
        return "usage: e_puttile <x> <y> <w> <h> <nth> <nmh> <mh> <sth> <smh> <lw> <rw> <tt> <mt> <bt> <b> <sp>";
    }
}
