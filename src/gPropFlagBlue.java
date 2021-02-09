import java.util.HashMap;

public class gPropFlagBlue extends gProp {
    HashMap<String, gDoableThing> gameModeEffects;

    public void propEffect(gPlayer p) {
        gDoableThing doable = gameModeEffects.get(cVars.get("gamemode"));
        if(doable != null)
            doable.doItem(p);
    }

    gDoableThing ctfDoable = new gDoableThing() {
        public void doItem(gThing p) {
            if(cVars.isVal("flagmasterid", uiInterface.uuid)) {
                cVars.put("flagmasterid", "");
                if(sSettings.net_server) {
                    xCon.ex("givepoint " + cGameLogic.userPlayer().get("id"));
                    xCon.ex("say " + sVars.get("playername") + " captured the flag!");
                }
            }
        }
    };

    public gPropFlagBlue(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.FLAGBLUE, ux, uy, x, y, w, h);
        gameModeEffects = new HashMap<>();
        gameModeEffects.put(Integer.toString(cGameMode.CAPTURE_THE_FLAG), ctfDoable);
    }
}
