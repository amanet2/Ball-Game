import java.util.HashMap;

public class gPropScorepoint extends gProp {
    HashMap<String, gDoableThing> gameModeEffects;

    public void propEffect(gPlayer p) {
        gDoableThing doable = gameModeEffects.get(cVars.get("gamemode"));
        if(doable != null)
            doable.doItem(p);
    }

    public gPropScorepoint(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.SCOREPOINT, ux, uy, x, y, w, h);
        gameModeEffects = new HashMap<>();
        gameModeEffects.put(Integer.toString(cGameMode.RACE), new gDoableThing() {
            public void doItem(gThing p) {
                HashMap scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                if(sSettings.net_server) {
                    if(!get("racebotidcheckins").contains(p.get("id"))) {
                        put("racebotidcheckins", get("racebotidcheckins")+(p.get("id")+":"));
                    }
                    int gonnaWin = 1;
                    for(Object id : scorepointsMap.keySet()) {
                        gThing pr = (gThing) scorepointsMap.get(id);
                        if(!pr.get("racebotidcheckins").contains(p.get("id"))) {
                            gonnaWin = 0;
                            break;
                        }
                    }
                    if(gonnaWin > 0) {
                        xCon.ex("givepoint "+p.get("id"));
                        cScripts.createScorePopup((gPlayer) p, 1);
                        for(Object id : scorepointsMap.keySet()) {
                            gProp pr = (gProp) scorepointsMap.get(id);
                            pr.put("racebotidcheckins",
                                    pr.get("racebotidcheckins").replace(p.get("id")+":", ""));
                        }
                    }
                }
                else {
                    if (isZero("int0")) {
                        putInt("int0", 1);
                        int gonnaWin = 1;
                        for(Object id : scorepointsMap.keySet()) {
                            gProp pr = (gProp) scorepointsMap.get(id);
                            if (pr.isZero("int0"))
                                gonnaWin = 0;
                        }
                        if (gonnaWin > 0) {
                            for(Object id : scorepointsMap.keySet()) {
                                gProp pr = (gProp) scorepointsMap.get(id);
                                pr.put("int0", "0");
                            }
                            cScripts.createScorePopup((gPlayer) p, 1);
                        }
                    }
                }
            }
        });
        gameModeEffects.put(Integer.toString(cGameMode.WAYPOINTS), new gDoableThing() {
            public void doItem(gThing p) {
                if(getInt("int0") > 0) {
                    put("int0", "0");
                    if(sSettings.net_server) {
                        xCon.ex("givepoint " + p.get("id"));
                        cGameMode.checkWaypoints();
                    }
                    else {
                        cScripts.createScorePopup((gPlayer) p,1);
                    }
                }
            }
        });
        gameModeEffects.put(Integer.toString(cGameMode.SAFE_ZONES), new gDoableThing() {
            public void doItem(gThing p) {
                //do safezones here
            }
        });
    }
}
