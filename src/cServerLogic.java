import java.util.HashMap;

public class cServerLogic {
    public static void gameLoop() {
        if(sVars.getInt("timelimit") > 0)
            cVars.putLong("timeleft",
                    sVars.getLong("timelimit") - (int) (uiInterface.gameTime - cVars.getLong("starttime")));
        else
            cVars.putLong("timeleft", -1);
        checkHealthStatus();
        checkForMapChange();
        checkGameState();
        eManager.updateEntityPositions();
    }

    public static void resetGameState() {
        gScoreboard.resetScoresMap();
        cVars.putLong("starttime", System.currentTimeMillis());
        cVars.put("gamewon", "0");
        cVars.put("winnerid","");
        switch (cVars.getInt("gamemode")) {
            case cGameLogic.VIRUS:
                cGameLogic.resetVirusPlayers();
                break;
            default:
                break;
        }
    }

    public static void checkGameState() {
        for(String id : eManager.getPlayerIds()) {
            //this shouldnt be needed, but when server user joins his own games, it is
            if(id.equals(uiInterface.uuid))
                continue;
            gPlayer obj = eManager.getPlayerById(id);
            for (int i = 0; i < 4; i++) {
                if(nServer.instance().clientArgsMap.get(obj.get("id")).containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(nServer.instance().clientArgsMap.get(
                            obj.get("id")).get("vels").split("-")[i]));
            }
        }
        if(nServer.instance().clientArgsMap.containsKey("server")
                && nServer.instance().clientArgsMap.get("server").containsKey("state")) {
            switch (cVars.getInt("gamemode")) {
                case cGameLogic.FLAG_MASTER:
                    if(eManager.currentMap.scene.getThingMap("ITEM_FLAG").size() > 0
                            && nServer.instance().clientArgsMap.get("server").get("state").length() > 0)
                        nServer.instance().addNetCmd("clearthingmap ITEM_FLAG");
                    if(nServer.instance().clientArgsMap.get("server").get("state").length() > 0
                            && cVars.getLong("flagmastertime") < uiInterface.gameTime) {
                        xCon.ex("givepoint " + nServer.instance().clientArgsMap.get("server").get("state"));
                        cVars.putLong("flagmastertime", uiInterface.gameTime + 1000);
                    }
                    break;
                case cGameLogic.VIRUS:
                    if(cVars.getLong("virustime") < uiInterface.gameTime) {
                        if(nServer.instance().clientArgsMap.containsKey("server")) {
                            if(nServer.instance().clientArgsMap.get("server").get("state").length() < 1) {
                                cGameLogic.resetVirusPlayers();
                            }
                            for(String id : eManager.getPlayerIds()) {
                                gPlayer p = eManager.getPlayerById(id);
                                if(nServer.instance().clientArgsMap.get("server").containsKey("state")
                                        && !nServer.instance().clientArgsMap.get("server").get("state").contains(id)) {
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
        HashMap<String, gThing> playerMap = eManager.currentMap.scene.getThingMap("THING_PLAYER");
        for (String playerId : playerMap.keySet()) {
            gPlayer player = (gPlayer) playerMap.get(playerId);
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
                    || (sVars.getInt("scorelimit") > 0 && gScoreboard.getWinnerScore() >= sVars.getInt("scorelimit"))) {
                cVars.put("gamewon", "1");
            }
            if(cVars.isOne("gamewon")) {
                //check for server win
                if(gScoreboard.isTopScoreId("server")) {
                    cVars.put("winnerid", "server");
                    nServer.instance().addNetCmd("echo " + sVars.get("playername") + " wins!");
                    gScoreboard.incrementScoreFieldById("server", "wins");
                }
                else {
                    //someone beats score
                    String highestId = gScoreboard.getWinnerId();
                    if(highestId.length() > 0) {
                        cVars.put("winnerid", highestId);
                        nServer.instance().addNetCmd("echo "
                                + nServer.instance().clientArgsMap.get(cVars.get("winnerid")).get("name") + " wins!");
                        gScoreboard.incrementScoreFieldById(cVars.get("winnerid"), "wins");
                    }
                }
                int toplay = (int) (Math.random() * eManager.winClipSelection.length);
                nServer.instance().addExcludingNetCmd("server",
                        "playsound sounds/win/"+eManager.winClipSelection[toplay]);
                cVars.putLong("intermissiontime",
                        System.currentTimeMillis() + Integer.parseInt(sVars.get("intermissiontime")));
            }
        }
    }

    public static void checkHealthStatus() {
        HashMap<String, HashMap<String, String>> argsMap = nServer.instance().clientArgsMap;
        Long currentTime = System.currentTimeMillis();
        for(String id : argsMap.keySet()) {
            if(!id.equals("server") && argsMap.get(id).containsKey("respawntime")
                    && Long.parseLong(argsMap.get(id).get("respawntime")) < currentTime) {
                xCon.ex("respawnnetplayer " + id);
                argsMap.get(id).remove("respawntime");
            }
        }
        //recharge players health
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

    public static void changeBotWeapon(gPlayer cl, int newweapon, boolean fromPowerup) {
        HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
        if(botsMap.size() > 0 && !(!fromPowerup && newweapon != 0)) {
            nServer.instance().clientArgsMap.get(cl.get("id")).put("weapon", Integer.toString(newweapon));
            cl.checkSpriteFlip();
        }
    }

    public static void checkHatStatus(){
        //player0
        gPlayer userPlayer = cClientLogic.getUserPlayer();
        if(userPlayer != null && !userPlayer.get("pathspritehat").contains(sVars.get("playerhat"))) {
            userPlayer.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",
                    sVars.get("playerhat"))));
        }
        //others
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            gPlayer p = eManager.getPlayerById(id);
            String chat = nServer.instance().clientArgsMap.get(id).get("hat");
            if(p == null || chat == null)
                continue;
            if(!p.get("pathspritehat").contains(chat)) {
                p.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",chat)));
                p.put("hat", chat);
            }
        }
    }

    public static void checkWeaponsStatus() {
        //other players
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = eManager.getPlayerById(id);
                int cweap = Integer.parseInt(nServer.instance().clientArgsMap.get(id).get("weapon"));
                if(!p.isInt("weapon", cweap))
                    p.putInt("weapon", cweap);
            }
        }
    }

    public static void checkForMapChange() {
        if(cVars.getLong("intermissiontime") > 0
                && cVars.getLong("intermissiontime") < System.currentTimeMillis()) {
            cVars.put("intermissiontime", "-1");
            cVars.putInt("timeleft", sVars.getInt("timelimit"));
            xCon.ex("changemaprandom");
        }
    }

    static void changeMap(String mapPath) {
        System.out.println("CHANGING MAP: " + mapPath);
        xCon.ex("clearthingmap THING_PLAYER");
        xCon.ex("exec maps/" + mapPath);
        nServer.instance().addExcludingNetCmd("server," + uiInterface.uuid,
                "clearthingmap THING_PLAYER;load;cv_maploaded 0");
        for(String id : nServer.instance().clientIds) {
            if(!id.equals(uiInterface.uuid))
                nServer.instance().sendMap(id);
            xCon.ex("respawnnetplayer " + id);
        }
    }
}
