import java.util.HashMap;

public class gPropFlagRed extends gProp {
    HashMap<String, gDoableThing> gameModeEffects;

    public void propEffect(gPlayer p) {
        gDoableThing doable = gameModeEffects.get(cVars.get("gamemode"));
        if(doable != null)
            doable.doItem(p);
    }

    gDoableThing ctfDoable = new gDoableThing() {
        public void doItem(gThing p) {
            if(sSettings.net_server && !get("str0").equalsIgnoreCase(p.get("id"))) {
                put("str0", p.get("id"));
                nServer.instance().addNetCmd("echo " + p.get("name") + " has the flag!");
            }
        }
    };

    gDoableThing flagMasterDoable = new gDoableThing() {
        public void doItem(gThing p) {
            if(sSettings.net_server && cVars.isVal("flagmasterid", "")) {
                cVars.put("flagmasterid", p.get("id"));
                xCon.ex("say " + p.get("name") + " has the flag!");
            }
        }
    };

    gDoableThing kingOfFlagsDoable = new gDoableThing() {
        public void doItem(gThing p) {
//            if(!isVal("str0", p.get("id")) ) {
//                String myId = get("id");
//                gProp myProp = (gProp) eManager.currentMap.scene.getThingMap("PROP_FLAGRED").get(myId);
//                //handle kingofflag flagred intersection
//                int pass = 1;
//                for(String id2 : gScene.getPlayerIds()) {
//                    gPlayer p2 = gScene.getPlayerById(id2);
//                    //make sure no other players still on the flag
//                    if(!p2.get("id").equals(p.get("id")) && p2.willCollideWithPropAtCoords(
//                            myProp, p2.getInt("coordx"), p2.getInt("coordy"))) {
//                        pass = 0;
//                        break;
//                    }
//                }
//                if(pass > 0) {
//                    put("str0", p.get("id"));
//                    if (sSettings.net_server) {
//                        xCon.ex("say " + p.get("name") + " captured flag#" + getInt("tag"));
//                        xCon.ex("givepoint " + p.get("id"));
//                    }
//                }
//            }
        }
    };

    public gPropFlagRed(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.FLAGRED, ux, uy, x, y, w, h);
        gameModeEffects = new HashMap<>();
        gameModeEffects.put(Integer.toString(cGameMode.CAPTURE_THE_FLAG), ctfDoable);
        gameModeEffects.put(Integer.toString(cGameMode.FLAG_MASTER), flagMasterDoable);
        gameModeEffects.put(Integer.toString(cGameMode.KING_OF_FLAGS), kingOfFlagsDoable);
    }
}
