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

        }
    };

    public gPropFlagRed(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.FLAGRED, ux, uy, x, y, w, h);
        gameModeEffects = new HashMap<>();
        gameModeEffects.put(Integer.toString(cGameMode.CAPTURE_THE_FLAG), flagMasterDoable);
        gameModeEffects.put(Integer.toString(cGameMode.FLAG_MASTER), flagMasterDoable);
        gameModeEffects.put(Integer.toString(cGameMode.KING_OF_FLAGS), kingOfFlagsDoable);
    }
}
