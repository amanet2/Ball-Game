public class gPropTeleporter extends gProp {
    public void propEffect(gPlayer p) {
        gProp exit = null;
        for(gProp pr : eManager.currentMap.scene.props()) {
            if(!pr.isVal("tag", get("tag")) && pr.isInt("code", gProp.TELEPORTER)
                    && pr.isVal("int0", get("int0"))) {
                exit = pr;
            }
        }
        if(exit != null) {
            if(p.get("id").contains("bot") && !p.isVal("exitteleportertag", get("tag"))) {
                p.putInt("coordx", exit.getInt("coordx") + exit.getInt("dimw")/2 - p.getInt("dimw")/2);
                p.putInt("coordy", exit.getInt("coordy") + exit.getInt("dimh")/2 - p.getInt("dimh")/2);
                p.put("exitteleportertag", exit.get("tag"));
            }
            else if(p.isZero("tag") && !cVars.isVal("exitteleportertag", get("tag"))) {
                xCon.ex("playsound sounds/teleporter.wav");
                p.putInt("coordx", exit.getInt("coordx") + exit.getInt("dimw")/2 - p.getInt("dimw")/2);
                p.putInt("coordy", exit.getInt("coordy") + exit.getInt("dimh")/2 - p.getInt("dimh")/2);
                cVars.put("exitteleportertag", exit.get("tag"));
            }
        }
    }

    public gPropTeleporter(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.TELEPORTER, ux, uy, x, y, w, h);
    }
}
