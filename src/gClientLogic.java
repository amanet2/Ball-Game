import java.util.HashMap;

public class gClientLogic {
    gScene scene;
    private static gPlayer userPlayer;

    public static void setUserPlayer(gPlayer newUserPlayer) {
        userPlayer = newUserPlayer;
        gCamera.centerCamera();
    }

    public static gPlayer getUserPlayer() {
        if(userPlayer == null)
            userPlayer = gScene.getPlayerById(uiInterface.uuid);
        return userPlayer;
    }

    public static boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", uiInterface.uuid);
    }

    public static void checkPlayerFire() {
        if(getUserPlayer() != null && iMouse.holdingMouseLeft)
            xCon.ex("attack");
    }

    public static void gameLoop() {
        oDisplay.instance().checkDisplay();
        oAudio.instance().checkAudio();
        gCamera.updatePosition();
        if(sSettings.show_mapmaker_ui)
            uiInterface.selectThingUnderMouse();
        checkGameState();
        checkMovementStatus();
        checkColorStatus();
        if(getUserPlayer() != null) {
            pointPlayerAtMousePointer();
            checkGameStateClient();
//                checkPlayersFire();
        }
    }

    public static void changeWeapon(int newweapon) {
        gPlayer p = gClientLogic.getUserPlayer();
        if(p != null) {
            if(newweapon != p.getInt("weapon"))
                xCon.ex("playsound sounds/grenpinpull.wav");
            p.putInt("weapon", newweapon);
            gClientLogic.getUserPlayer().checkSpriteFlip();
        }
    }

    public static void playPlayerDeathSound() {
        double r = Math.random();
        if(r > .99)
            xCon.ex("playsound sounds/growl.wav");
        else if(r > .49)
            xCon.ex("playsound sounds/shout.wav");
        else
            xCon.ex("playsound sounds/death.wav");
    }

    public static void checkColorStatus(){
        //check all id colors, including yours
        for(String id : nClient.instance().serverArgsMap.keySet()) {
            gPlayer p = gScene.getPlayerById(id);
            String ccol = nClient.instance().serverArgsMap.get(id).get("color");
            if(p == null || ccol == null)
                continue;
            if(!p.get("pathsprite").contains(ccol)) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", ccol,
                        p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
            }
        }
    }

    public static void pointPlayerAtMousePointer() {
        gPlayer p = getUserPlayer();
        int[] mc = uiInterface.getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.getInt("coordx") + p.getInt("dimw")/2
                - cVars.getInt("camx"));
        double dy = mc[1] - eUtils.scaleInt(p.getInt("coordy") + p.getInt("dimh")/2
                - cVars.getInt("camy"));
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.putDouble("fv", angle);
        p.checkSpriteFlip();
    }

    public static void checkGameStateClient() {
        if(nClient.instance().serverArgsMap.containsKey("server")
                && nClient.instance().serverArgsMap.get("server").containsKey("state")) {
            //gamestate checks, for server AND clients
            //check to delete flags that should not be present anymore
            if (eManager.currentMap.scene.getThingMap("ITEM_FLAG").size() > 0
                    && nClient.instance().serverArgsMap.get("server").get("state").length() > 0)
                xCon.ex("clearthingmap ITEM_FLAG");
        }
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
                    p.checkSpriteFlip();
                }
                for(int i = 0; i < cmovedirs.length; i++) {
                    if(p.getInt("mov"+i) != Character.getNumericValue(cmovedirs[i]))
                        p.putInt("mov"+i, Character.getNumericValue(cmovedirs[i]));
                }
            }
        }
    }

    public static void doCommand(String cmd) {
        nClient.instance().addNetCmd(cmd);
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
