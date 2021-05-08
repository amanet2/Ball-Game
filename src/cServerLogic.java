import java.util.HashMap;

public class cServerLogic {
    public static void checkGameState() {
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
}
