import java.util.HashMap;

public class gClientLogic {
    gScene scene;
    private static gPlayer userPlayer;

    public static void setUserPlayer(gPlayer newUserPlayer) {
        userPlayer = newUserPlayer;
        cScripts.centerCamera();
    }

    public static gPlayer getUserPlayer() {
        if(userPlayer == null)
            userPlayer = gScene.getPlayerById(uiInterface.uuid);
        return userPlayer;
    }

    public static void gameLoop() {
        oDisplay.instance().checkDisplay();
        oAudio.instance().checkAudio();
        gCamera.updatePosition();
        if(sSettings.show_mapmaker_ui)
            cScripts.selectThingUnderMouse();
        checkGameState();
        checkMovementStatus();
    }

    //clientside prediction for movement aka smoothing
    public static void checkMovementStatus() {
        //other players
        for(String id : eManager.currentMap.scene.playersMap().keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                String[] requiredFields = new String[]{"fv", "dirs", "x", "y"};
                if(!nClient.instance().containsArgsForId(id, requiredFields))
                    continue;
                HashMap<String, String> cargs = nClient.instance().serverArgsMap.get(id);
                double cfv = Double.parseDouble(cargs.get("fv"));
                char[] cmovedirs = cargs.get("dirs").toCharArray();
                gPlayer p = gScene.getPlayerById(id);
                if(p == null)
                    return;
                if(sVars.isZero("smoothing")) {
                    p.put("coordx", cargs.get("x"));
                    p.put("coordy", cargs.get("y"));
                }
                if(p.getDouble("fv") != cfv) {
                    p.putDouble("fv", cfv);
                    cScripts.checkPlayerSpriteFlip(p);
                }
                for(int i = 0; i < cmovedirs.length; i++) {
                    if(p.getInt("mov"+i) != Character.getNumericValue(cmovedirs[i]))
                        p.putInt("mov"+i, Character.getNumericValue(cmovedirs[i]));
                }
            }
        }
    }

    static void checkGameState() {
        for(String id : gScene.getPlayerIds()) {
            if(id.equals(uiInterface.uuid) || !nClient.instance().serverArgsMap.containsKey(id))
                continue;
            gPlayer obj = gScene.getPlayerById(id);
            for (int i = 0; i < 4; i++) {
                if(nClient.instance().serverArgsMap.get(id).containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(nClient.instance().serverArgsMap.get(
                            obj.get("id")).get("vels").split("-")[i]));
            }
        }
    }
}
