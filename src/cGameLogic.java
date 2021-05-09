import java.util.HashMap;

public class cGameLogic {

    private static gPlayer userPlayer;

    public static void setUserPlayer(gPlayer newUserPlayer) {
        userPlayer = newUserPlayer;
        cScripts.centerCamera();
    }

    public static gPlayer userPlayer() {
        if(userPlayer == null)
            userPlayer = gScene.getPlayerById(uiInterface.uuid);
        return userPlayer;
    }

    /**
     * executed at every game tick
     */
    public static void customLoop() {
        try {
//            checkHatStatus();
//            checkColorStatus();
            checkMovementStatus();
            if(userPlayer() != null) {
//                cScripts.pointPlayerAtMousePointer();
                checkGameStateClient();
//                checkPlayersFire();
            }
//            cScripts.checkBulletSplashes();
        }
        catch(Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static void resetGameState() {
        cScoreboard.resetScoresMap();
        cVars.putLong("starttime", System.currentTimeMillis());
        cVars.put("gamewon", "0");
        cVars.put("winnerid","");
        switch (cVars.getInt("gamemode")) {
            case cGameMode.VIRUS:
                cGameMode.resetVirusPlayers();
                break;
            default:
                break;
        }
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

    public static void checkMovementStatus() {
        //other players
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                String[] requiredFields = new String[]{"fv", "dirs", "x", "y"};
                if(!nServer.instance().containsArgsForId(id, requiredFields))
                    continue;
                HashMap<String, String> cargs = nServer.instance().clientArgsMap.get(id);
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

    public static void checkHatStatus(){
        //player0
        gPlayer userPlayer = cGameLogic.userPlayer();
        if(userPlayer != null && !userPlayer.get("pathspritehat").contains(sVars.get("playerhat"))) {
            userPlayer.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",
                    sVars.get("playerhat"))));
        }
        //others
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            gPlayer p = gScene.getPlayerById(id);
            String chat = nServer.instance().clientArgsMap.get(id).get("hat");
            if(p == null || chat == null)
                continue;
            if(!p.get("pathspritehat").contains(chat)) {
                p.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",chat)));
                p.put("hat", chat);
            }
        }
    }

    public static void checkColorStatus(){
        //check all id colors, including yours
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            gPlayer p = gScene.getPlayerById(id);
            String ccol = nServer.instance().clientArgsMap.get(id).get("color");
            if(p == null || ccol == null)
                continue;
            if(!p.get("pathsprite").contains(ccol)) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", ccol,
                        p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
            }
        }
    }

    public static void checkWeaponsStatus() {
        //other players
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = gScene.getPlayerById(id);
                int cweap = Integer.parseInt(nServer.instance().clientArgsMap.get(id).get("weapon"));
                if(!p.isInt("weapon", cweap))
                    p.putInt("weapon", cweap);
            }
        }
    }

    public static void doCommand(String cmd) {
        if(sSettings.IS_CLIENT)
            nClient.instance().addNetCmd(cmd);
        else
            xCon.ex(cmd);
    }

    public static boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", uiInterface.uuid);
    }

    public static void rechargePlayersHealth() {
        HashMap playersMap = eManager.currentMap.scene.getThingMap("THING_PLAYER");
        for(Object id : playersMap.keySet()) {
            gPlayer p = (gPlayer) playersMap.get(id);
            if(p.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                    p.getLong("hprechargetime")+cVars.getInt("delayhp") < System.currentTimeMillis()) {
                if(p.getInt("stockhp")+cVars.getInt("rechargehp") > cVars.getInt("maxstockhp"))
                    p.put("stockhp", cVars.get("maxstockhp"));
                else
                    p.putInt("stockhp", p.getInt("stockhp") + cVars.getInt("rechargehp"));
            }
        }
    }

    public static void checkHealthStatus() {
        HashMap<String, HashMap<String, String>> argsMap = nServer.instance().clientArgsMap;
        Long currentTime = System.currentTimeMillis();
        for(String id : argsMap.keySet()) {
            if(!id.equals("server") && argsMap.get(id).containsKey("respawntime")
            && Long.parseLong(argsMap.get(id).get("respawntime")) < currentTime) {
                nServer.instance().addNetCmd("respawnnetplayer " + id);
                argsMap.get(id).remove("respawntime");
            }
        }
        rechargePlayersHealth();
    }

    public static void checkForMapChange() {
        if (cVars.getLong("intermissiontime") > 0
                && cVars.getLong("intermissiontime") < System.currentTimeMillis()) {
            cVars.put("intermissiontime", "-1");
            cVars.putInt("timeleft", sVars.getInt("timelimit"));
            xCon.ex("changemaprandom");
        }
    }

    public static void checkPlayersFire() {
        if(cGameLogic.userPlayer() != null && iMouse.holdingMouseLeft)
                xCon.ex("attack");
    }
}
