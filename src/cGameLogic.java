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
            if (sSettings.NET_MODE == sSettings.NET_SERVER) {
                nServer.instance().checkForUnhandledQuitters();
                checkHealthStatus();
                checkForMapChange();
                checkGameState();
            }
            checkHatStatus();
            checkColorStatus();
            checkMovementStatus();
            if(userPlayer() != null) {
                cScripts.pointPlayerAtMousePointer();
                checkGameState();
                checkPlayersFire();
            }
            cScripts.checkBulletSplashes();
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
                if(p.getDouble("fv") != cfv || sSettings.isServer()) {
                    //when you're the server, p.getDouble("fv") ALWAYS == cfv
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
        switch (sSettings.NET_MODE) {
            case sSettings.NET_SERVER:
                nServer.instance().addNetCmd(cmd);
                break;
            case sSettings.NET_CLIENT:
                nClient.instance().addNetCmd(cmd);
                break;
            case sSettings.NET_OFFLINE:
                xCon.ex(cmd);
        }
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
        if (sSettings.isServer()) {
            if (cVars.getLong("intermissiontime") > 0
                    && cVars.getLong("intermissiontime") < System.currentTimeMillis()) {
                cVars.put("intermissiontime", "-1");
                cVars.putInt("timeleft", sVars.getInt("timelimit"));
                xCon.ex("changemaprandom");
            }
        }
    }

    public static void checkPlayersFire() {
        if(cGameLogic.userPlayer() != null && iMouse.holdingMouseLeft)
                xCon.ex("attack");
    }

    public static void checkGameState() {
        if(sSettings.isServer() && nServer.instance().clientArgsMap.containsKey("server")
        && nServer.instance().clientArgsMap.get("server").containsKey("state")) {
            switch (cVars.getInt("gamemode")) {
                case cGameMode.FLAG_MASTER:
                    if(eManager.currentMap.scene.getThingMap("ITEM_FLAG").size() > 0
                            && nServer.instance().clientArgsMap.get("server").get("state").length() > 0)
                        nServer.instance().addNetCmd("clearthingmap ITEM_FLAG");
                    if(nServer.instance().clientArgsMap.get("server").get("state").length() > 0
                            && cVars.getLong("flagmastertime") < uiInterface.gameTime) {
                        xCon.ex("givepoint " + nServer.instance().clientArgsMap.get("server").get("state"));
                        cVars.putLong("flagmastertime", uiInterface.gameTime + 1000);
                    }
                    break;
                case cGameMode.VIRUS:
                    if(cVars.getLong("virustime") < uiInterface.gameTime) {
                        if(nServer.instance().clientArgsMap.containsKey("server")) {
                            if(nServer.instance().clientArgsMap.get("server").get("state").length() < 1) {
                                cGameMode.resetVirusPlayers();
                            }
                            for(String id : gScene.getPlayerIds()) {
                                gPlayer p = gScene.getPlayerById(id);
                                if(nServer.instance().clientArgsMap.get("server").containsKey("state")
                                        && !nServer.instance().clientArgsMap.get("server").get("state").contains(id)
                                        && p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000) {
                                    xCon.ex("givepoint " + p.get("id"));
                                }
                            }
                        }
                        cVars.putLong("virustime", uiInterface.gameTime + 1000);
                    }
                    break;
                default:
                    break;
            }
        }
        else if(sSettings.isClient() && nServer.instance().clientArgsMap.containsKey("server")
                && nServer.instance().clientArgsMap.get("server").containsKey("state")) {
            //gamestate checks, for server AND clients
            //check to delete flags that should not be present anymore
            if (eManager.currentMap.scene.getThingMap("ITEM_FLAG").size() > 0
                    && nServer.instance().clientArgsMap.get("server").get("state").length() > 0)
                xCon.ex("clearthingmap ITEM_FLAG");
        }
        // NEW ITEMS CHECKING.  ACTUALLY WORKS
        if(sSettings.isServer() || (sSettings.isOffline()  && cGameLogic.userPlayer() != null)) {
            HashMap<String, gPlayer> playerMap = eManager.currentMap.scene.playersMap();
            for (String playerId : playerMap.keySet()) {
                gPlayer player = playerMap.get(playerId);
                //check null fields
                if (!player.containsFields(new String[]{"coordx", "coordy"}))
                    break;
                //check player teleporters
                int clearTeleporterFlag = 1;
                for(String checkType : eManager.currentMap.scene.objectMaps.keySet()) {
                    if(checkType.contains("ITEM_") && !checkType.equals("ITEM_SPAWNPOINT")) {
                        HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap(checkType);
                        for (String itemId : thingMap.keySet()) {
                            gItem item = (gItem) thingMap.get(itemId);
                            if (player.willCollideWithThingAtCoords(item,
                                    player.getInt("coordx"),
                                    player.getInt("coordy"))) {
                                item.activateItem(player);
                                if(checkType.contains("ITEM_TELEPORTER"))  {
                                    clearTeleporterFlag = 0;
                                }
                            }
                        }
                    }
                }
                //after checking all items
                if(clearTeleporterFlag > 0) {
                    player.put("inteleporter", "0");
                }
            }
        }
        //check for winlose
        if(sSettings.isServer() && !sSettings.show_mapmaker_ui && cVars.isZero("gamewon")) {
            //conditions
            if((cVars.getInt("timeleft") > -1 && cVars.getInt("timeleft") < 1
                    && cVars.getLong("intermissiontime") < 0)
            || (sVars.getInt("scorelimit") > 0 && cScoreboard.getWinnerScore() >= sVars.getInt("scorelimit"))) {
                cVars.put("gamewon", "1");
            }
            if(cVars.isOne("gamewon")) {
                //check for server win
                if(cScoreboard.isTopScoreId("server")) {
                    cVars.put("winnerid", "server");
                    nServer.instance().addNetCmd("echo " + sVars.get("playername") + " wins!");
                    cScoreboard.incrementScoreFieldById("server", "wins");
                }
                else {
                    //someone beats score
                    String highestId = cScoreboard.getWinnerId();
                    if(highestId.length() > 0) {
                        cVars.put("winnerid", highestId);
                        nServer.instance().addNetCmd("echo "
                                + nServer.instance().clientArgsMap.get(cVars.get("winnerid")).get("name") + " wins!");
                        if(sSettings.isServer())
                            cScoreboard.incrementScoreFieldById(cVars.get("winnerid"), "wins");
                    }
                }
                int toplay = (int) (Math.random() * eManager.winClipSelection.length);
                nServer.instance().addNetCmd("playsound sounds/win/"+eManager.winClipSelection[toplay]);
                cScripts.goToEndScreen();
            }
        }
    }
}
