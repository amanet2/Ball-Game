public class xComPutTile extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 14) {
            int x = Integer.parseInt(toks[4]);
            int y = Integer.parseInt(toks[5]);
            int w = Integer.parseInt(toks[6]);
            int h = Integer.parseInt(toks[7]);
            int nth = Integer.parseInt(toks[8]);
            int nmh = Integer.parseInt(toks[9]);
            int mh = Integer.parseInt(toks[10]);
            int sth = Integer.parseInt(toks[11]);
            int smh = Integer.parseInt(toks[12]);
            int lh = Integer.parseInt(toks[13]);
            int rh = Integer.parseInt(toks[14]);
            String tt = toks[1];
            String mt = toks[2];
            String bt = toks[3];
            int bl = Integer.parseInt(toks[15]);
            gTile t = new gTile(x, y, w, h, nth, nmh, mh, sth, smh, lh, rh, eUtils.getPath(tt),
                    eUtils.getPath(mt), eUtils.getPath(bt), bl);
            t.putInt("id", eManager.currentMap.scene.tiles().size());
            eManager.currentMap.scene.tiles().add(t);
            return "";
        }
        return "usage: puttile <x> <y> <w> <h> <nth> <nmh> <mh> <sth> <smh> <lw> <rw> <tt> <mt> <bt> <b> <sp>";
    }
}
