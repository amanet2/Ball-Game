import java.util.HashMap;

public class gPropFlagRed extends gProp {
    HashMap<String, gDoableThing> gameModeEffects;

    public void propEffect(gPlayer p) {
        gDoableThing doable = gameModeEffects.get(cVars.get("gamemode"));
        if(doable != null)
            doable.doItem(p);
    }

    gDoableThing flagMasterDoable = new gDoableThing() {
        public void doItem(gThing p) {
            if(cVars.isVal("flagmasterid", "")) {
                cVars.put("flagmasterid", uiInterface.uuid);
                if(sSettings.net_server) {
                    xCon.ex("say " + sVars.get("playername") + " has the flag!");
                }
            }
        }
    };

    gDoableThing kingOfFlagsDoable = new gDoableThing() {
        public void doItem(gThing p) {
//            if(!isVal("str0", cGameLogic.getUserPlayer().get("id"))) {
//                gPlayer cl = cGameLogic.getUserPlayer();
//                int pass = 1;
//                for(gPlayer p2 : eManager.currentMap.scene.players()) {
//                    //make sure no other players still on the flag
//                    if(!p2.get("id").equals(cl.get("id"))
//                            && p2.willCollideWithPropAtCoords(this, p2.getInt("coordx"), p2.getInt("coordy"))) {
//                        pass = 0;
//                        break;
//                    }
//                }
//                if(pass > 0) {
//                    put("str0", cl.get("id"));
//                    if(sSettings.net_server) {
//                        xCon.ex("givepoint " + cGameLogic.getUserPlayer().get("id"));
//                        xCon.ex("say " + sVars.get("playername") + " captured flag#"+getInt("tag"));
//                    }
//                    cScripts.createScorePopup(cGameLogic.getUserPlayer(),1);
//                }
//            }
        }
    };

    public gPropFlagRed(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.FLAGRED, ux, uy, x, y, w, h);
        gameModeEffects = new HashMap<>();
        gameModeEffects.put(Integer.toString(cGameMode.CAPTURE_THE_FLAG), flagMasterDoable);
        gameModeEffects.put(Integer.toString(cGameMode.FLAG_MASTER), flagMasterDoable);
        gameModeEffects.put(Integer.toString(cGameMode.KING_OF_FLAGS), kingOfFlagsDoable);
    }
}
