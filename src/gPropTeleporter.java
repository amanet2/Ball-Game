public class gPropTeleporter extends gProp {
    public gProp load(String[] args) {
        gPropTeleporter prop = new gPropTeleporter(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                Integer.parseInt(args[6]));
        prop.putInt("tag", eManager.currentMap.scene.teleportersMap().size());
        prop.putInt("native", 1);
        eManager.currentMap.scene.props().add(prop);
        return prop;
    }
    public void propEffect(gPlayer p) {
        gProp exit = null;
        for(String id : eManager.currentMap.scene.teleportersMap().keySet()) {
            gProp pr = (gProp) eManager.currentMap.scene.teleportersMap().get(id);
            if(!pr.isVal("tag", get("tag")) && pr.isVal("int0", get("int0"))) {
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
