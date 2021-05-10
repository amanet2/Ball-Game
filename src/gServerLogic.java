import java.util.HashMap;

public class gServerLogic {
    gScene scene;
    public static void gameLoop() {
        if(sVars.getInt("timelimit") > 0)
            cVars.putLong("timeleft",
                    sVars.getLong("timelimit") - (int) (uiInterface.gameTime - cVars.getLong("starttime")));
        else
            cVars.putLong("timeleft", -1);
//        cGameLogic.checkHealthStatus();
        checkForMapChange();
        checkGameState();
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

    public static void checkGameState() {
        for(String id : gScene.getPlayerIds()) {
            //this shouldnt be needed, but when server user joins his own games, it is
            if(id.equals(uiInterface.uuid))
                continue;
            gPlayer obj = gScene.getPlayerById(id);
            for (int i = 0; i < 4; i++) {
                if(nServer.instance().clientArgsMap.get(obj.get("id")).containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(nServer.instance().clientArgsMap.get(
                            obj.get("id")).get("vels").split("-")[i]));
            }
        }

        if(nServer.instance().clientArgsMap.containsKey("server")
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
        // NEW ITEMS CHECKING.  ACTUALLY WORKS
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
        //check for winlose
        if(!sSettings.show_mapmaker_ui && cVars.isZero("gamewon")) {
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
                        cScoreboard.incrementScoreFieldById(cVars.get("winnerid"), "wins");
                    }
                }
                int toplay = (int) (Math.random() * eManager.winClipSelection.length);
                nServer.instance().addNetCmd("playsound sounds/win/"+eManager.winClipSelection[toplay]);
                cScripts.goToEndScreen();
            }
        }
    }

    public static void checkForMapChange() {
        if (cVars.getLong("intermissiontime") > 0
                && cVars.getLong("intermissiontime") < System.currentTimeMillis()) {
            cVars.put("intermissiontime", "-1");
            cVars.putInt("timeleft", sVars.getInt("timelimit"));
            xCon.ex("changemaprandom");
        }
    }

    static void changeMap(String mapPath) {
        System.out.println("CHANGING MAP: " + mapPath);
        xCon.ex("exec maps/" + mapPath);
        nServer.instance().addExcludingNetCmd("server", "clearthingmap THING_PLAYER;cv_maploaded 0;load");
        for(String id : nServer.instance().clientIds) {
            nServer.instance().sendMap(id);
            String postString = String.format("spawnplayer %s %s %s",
                    gClientLogic.getUserPlayer().get("id"),
                    gClientLogic.getUserPlayer().get("coordx"),
                    gClientLogic.getUserPlayer().get("coordy")
            );
            nServer.instance().addNetCmd(id, postString);
            xCon.ex("respawnnetplayer " + id);
        }
    }
}
