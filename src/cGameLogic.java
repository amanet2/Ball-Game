import java.util.ArrayList;
import java.util.HashMap;

public class cGameLogic {

    private static gPlayer userPlayer;

    public static void setUserPlayer(gPlayer newUserPlayer) {
        userPlayer = newUserPlayer;
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
            if(cVars.isOne("quitconfirmed")) {
                uiInterface.exit();
            }
            if(sSettings.net_server) {
                checkQuitterStatus();
                checkHealthStatus();
                checkHatStatus();
                checkColorStatus();
                checkForMapChange();
                checkGameState();
            }
            else if(sSettings.net_client) {
                checkQuitterStatus();
                if(userPlayer() == null) {
                    checkHatStatus(); //for spectator mode
                    checkColorStatus(); //for spectator
                }
            }
            checkMovementStatus();

            if(userPlayer() != null) {
                // methods here need migrating to server
                checkMapGravity();
                cScripts.pointPlayerAtMousePointer();
                checkHatStatus();
                checkColorStatus();
                checkSprintStatus();
                checkPowerupsStatus();
                checkGameState();
                checkPlayersFire();
//                checkForPlayerDeath(); //OLD used for sidescroller falling and safezones
            }
            cScripts.checkBulletSplashes();
        }
        catch(Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static void checkPowerupsStatus() {
        if(sSettings.net_server || !cScripts.isNetworkGame()) {
            if (cVars.getLong("powerupstime") < System.currentTimeMillis()) {
                int powerupson = 0;
                ArrayList<gProp> powerupcandidates = new ArrayList<>();
                HashMap<String, gThing> powerupsMap = eManager.currentMap.scene.getThingMap("PROP_POWERUP");
                for (String id : powerupsMap.keySet()) {
                    gProp p = (gProp) powerupsMap.get(id);
                    if (!p.isZero("int0")) {
                        powerupson++;
                    }
                    else if(p.isOne("native")){
                        powerupcandidates.add(p);
                    }
                }
                int ctr = 0;
                int limit = Math.min(powerupcandidates.size(), cVars.getInt("powerupson")-powerupson);
                while (ctr < limit) {
                    int r = (int) (Math.random() * powerupcandidates.size());
                    int rr = (int) (Math.random() * gWeapons.weaponSelection().size()-1)+1;
                    powerupcandidates.get(r).put("int0", Integer.toString(rr));
                    powerupcandidates.get(r).putInt("int1", gWeapons.fromCode(rr).maxAmmo);
                    powerupcandidates.remove(r);
                    ctr++;
                }
                cVars.putLong("powerupstime", System.currentTimeMillis() + sVars.getLong("powerupswaittime"));
            }
        }
        if(sSettings.net_server) {
            for(String id : gScene.getPlayerIds()) {
                if(id.contains("bot")) {
                    gPlayer p = gScene.getPlayerById(id);
                    if (p.getLong("powerupsusetime") < System.currentTimeMillis()) {
                        if (p.getInt("weapon") != gWeapons.type.NONE.code()) {
                            cScripts.changeBotWeapon(p, gWeapons.type.NONE.code(), true);
                        }
                    }
                }
            }
        }
    }

    public static void checkMapGravity() {
            if(cVars.isOne("clipplayer")) {
                gPlayer userPlayer = cGameLogic.userPlayer();
                if(userPlayer == null)
                    return;
                if (cVars.isOne("jumping")) {
                    userPlayer.putInt("mov1", 0);
                    userPlayer.putInt("vel0", cVars.getInt("gravity"));
                }
                else {
                    if(!userPlayer.contains("respawntime")) {
                        if(userPlayer.isOne("crouch"))
                            userPlayer.subtractVal("vel1",
                                    userPlayer.getInt("vel1") > 1 ? 1 : 0); //want vel1 to be 1 while crouching
                        else
                            userPlayer.addVal("vel1",
                                    userPlayer.getInt("vel1") < cVars.getInt("gravity")
                                            && cVars.getInt("gravity") > 0
                            ? 1 : 0);
                    }
                    cVars.put("jumpheight", "0");
                }
            }
            //jumping
            if(cVars.isOne("clipplayer")) {
                if(cVars.isOne("jumping") && cVars.getInt("jumpheight") < cVars.getInt("jumptimemax")) {
                    cVars.increment("jumpheight");
                    if(cVars.getInt("jumpheight") > cVars.getInt("jumpsquish"))
                        xCon.ex("-crouch");
                }
                else if(cVars.isOne("jumping")) {
//                    if(cVars.isInt("mapview", gMap.MAP_SIDEVIEW))
//                        userPlayer().putInt("mov0", 0);
                    cVars.putInt("jumping", 0);
                }
            }
    }

//    public static boolean drawLocalSpawnProtection() {
//        gPlayer userplayer = cGameLogic.userPlayer();
//        return userplayer != null && (userplayer.contains("spawnprotectiontime")
//                && userplayer.getLong("spawnprotectiontime") > System.currentTimeMillis());
//    }

    public static void resetGameState() {
        cScoreboard.resetScoresMap();
        cVars.putLong("starttime", System.currentTimeMillis());
        cVars.put("gamewon", "0");
        cVars.put("winnerid","");
        cVars.put("flagmasterid", "");
        switch (cVars.getInt("gamemode")) {
            case cGameMode.KING_OF_FLAGS:
                cGameMode.resetKingOfFlags();
                break;
            case cGameMode.WAYPOINTS:
                cGameMode.refreshWaypoints();
                break;
            case cGameMode.SAFE_ZONES:
                cGameMode.refreshSafeZones();
                break;
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
                String[] requiredFields = new String[]{"fv", "dirs", "crouch", "flashlight", "x", "y"};
                if(!nServer.instance().containsArgsForId(id, requiredFields))
                    continue;
                HashMap<String, String> cargs = nServer.instance().clientArgsMap.get(id);
                double cfv = Double.parseDouble(cargs.get("fv"));
                char[] cmovedirs = cargs.get("dirs").toCharArray();
                int ccrouch = Integer.parseInt(cargs.get("crouch"));
                int cflashlight = Integer.parseInt(cargs.get("flashlight"));
                gPlayer p = gScene.getPlayerById(id);
                if(p == null)
                    return;
                if(sVars.isZero("smoothing")) {
                    p.put("coordx", cargs.get("x"));
                    p.put("coordy", cargs.get("y"));
                }
                if(p.getDouble("fv") != cfv || sSettings.net_server) {
                    //when you're the server, p.getDouble("fv") ALWAYS == cfv
                    p.putDouble("fv", cfv);
                    cScripts.checkPlayerSpriteFlip(p);
                }
                for(int i = 0; i < cmovedirs.length; i++) {
                    if(p.getInt("mov"+i) != Character.getNumericValue(cmovedirs[i]))
                        p.putInt("mov"+i, Character.getNumericValue(cmovedirs[i]));
                }
                if(!p.isInt("crouch", ccrouch))
                    p.putInt("crouch", ccrouch);
                if(!p.isInt("flashlight", cflashlight))
                    p.putInt("flashlight", cflashlight);
            }
        }
    }

    public static void checkDisconnectStatus() {
        if(sSettings.isServer() && cVars.isOne("disconnecting")) {
            nServer.instance().disconnect();
        }
        else if(sSettings.isClient() && cVars.isOne("disconnectconfirmed")) {
            nClient.instance().disconnect();
        }
    }

    public static void checkQuitterStatus() {
        switch (sSettings.NET_MODE) {
            case sSettings.NET_SERVER:
                nServer.instance().checkForUnhandledQuitters();
                break;
            case sSettings.NET_CLIENT:
                checkDisconnectStatus();
                break;
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

    public static boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", uiInterface.uuid);
    }

    public static void checkHealthStatus() {
        HashMap<String, HashMap<String, String>> argsMap = nServer.instance().clientArgsMap;
        Long currentTime = System.currentTimeMillis();
        for(String id : argsMap.keySet()) {
            if(!id.equals("server") && argsMap.get(id).containsKey("respawntime")
            && Long.parseLong(argsMap.get(id).get("respawntime")) < currentTime) {
                if(!id.contains("bot"))
                    nServer.instance().addNetCmd("respawnplayer " + id);
                else
                    xCon.ex("respawnplayer " + id);
                argsMap.get(id).remove("respawntime");
            }
        }
        HashMap playersMap = eManager.currentMap.scene.getThingMap("THING_PLAYER");
        for(Object id : playersMap.keySet()) {
            gPlayer p = (gPlayer) playersMap.get(id);
            //server-side respawn code to be enabled after refactoring completed
            if(p.contains("respawntime") && (p.getLong("respawntime") < currentTime
                    || cVars.get("winnerid").length() > 0 || cVars.getInt("timeleft") <= 0)) {
                p.remove("respawntime");
            }
            if(p.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                    p.getLong("hprechargetime")+cVars.getInt("delayhp") < System.currentTimeMillis()) {
                if(p.getInt("stockhp")+cVars.getInt("rechargehp") > cVars.getInt("maxstockhp"))
                    p.put("stockhp", cVars.get("maxstockhp"));
                else
                    p.putInt("stockhp", p.getInt("stockhp") + cVars.getInt("rechargehp"));
            }
        }
    }

    public static void checkSprintStatus() {
//        if(cVars.isZero("sprint") && cVars.getInt("stockspeed") < cVars.getInt("maxstockspeed")) {
        if(cVars.isZero("sprint") && cVars.getInt("stockspeed") < cVars.getInt("maxstockspeed") &&
            cVars.getLong("sprinttime") + cVars.getInt("delaypow") < System.currentTimeMillis()) {
            if(cVars.getInt("stockspeed") + cVars.getInt("rechargepow") > cVars.getInt("maxstockspeed"))
                cVars.put("stockspeed", cVars.get("maxstockspeed"));
            else
                cVars.putInt("stockspeed", cVars.getInt("stockspeed") + cVars.getInt("rechargepow"));
        }
        else if(cVars.isOne("sprint") && cVars.getInt("stockspeed") > 0) {
            if(cVars.getLong("sprinttime") < System.currentTimeMillis() || cVars.getInt("stockspeed") < 1) {
                cVars.put("sprint", "0");
                cVars.put("stockspeed", "0");
            }
            else {
                cVars.putInt("stockspeed", (int)(cVars.getLong("sprinttime")-System.currentTimeMillis()));
            }
        }
        else if(cVars.getInt("stockspeed") < 1) {
            cVars.put("sprint", "0");
            cVars.put("stockspeed", "0");
        }
    }

    public static void checkForMapChange() {
        if((sSettings.net_server && cVars.getLong("intermissiontime") > 0
                && cVars.getLong("intermissiontime") < System.currentTimeMillis())) {
            cVars.put("intermissiontime", "-1");
            cVars.putInt("timeleft", sVars.getInt("timelimit"));
            xCon.ex("changemaprandom");
        }
    }

    public static void checkPlayersFire() {
        if(cGameLogic.userPlayer() != null && iMouse.holdingMouseLeft)
                xCon.ex("attack");
    }

    public static void checkGameState() {
        if(sSettings.net_server) {
            switch (cVars.getInt("gamemode")) {
                case cGameMode.FLAG_MASTER:
                    if(eManager.currentMap.scene.getThingMap("ITEM_FLAG").size() > 0
                            && cVars.get("flagmasterid").length() > 0)
                        nServer.instance().addNetCmd("clearthingmap ITEM_FLAG");
                    if(cVars.get("flagmasterid").length() > 0
                            && cVars.getLong("flagmastertime") < uiInterface.gameTime) {
                        xCon.ex("givepoint " + cVars.get("flagmasterid"));
                        cVars.putLong("flagmastertime", uiInterface.gameTime + 1000);
                    }
                    break;
                case cGameMode.KING_OF_FLAGS:
                    cGameMode.checkKingOfFlags();
                    break;
                case cGameMode.VIRUS:
                    cGameMode.checkVirus();
                    break;
                default:
                    break;
            }
        }
        else if(sSettings.isClient()) {
            //gamestate checks, for server AND clients
            //check to delete flags that should not be present anymore
            if (eManager.currentMap.scene.getThingMap("ITEM_FLAG").size() > 0
                    && cVars.get("flagmasterid").length() > 0)
                xCon.ex("clearthingmap ITEM_FLAG");
        }
        // NEW ITEMS CHECKING.  ACTUALLY WORKS
        if(sSettings.net_server || (sSettings.NET_MODE == sSettings.NET_OFFLINE && cGameLogic.userPlayer() != null)) {
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
        //
        // NEW ABOVE, OLD BELOW
        //
        //check ALL PROPS this is the best one
        //new way of checkingProps
        HashMap<String, gPlayer> playerMap = eManager.currentMap.scene.playersMap();
        for(String checkThingType : new String[]{
                "PROP_TELEPORTER", "PROP_BOOST", "PROP_POWERUP", "PROP_SCOREPOINT", "PROP_FLAGRED", "PROP_FLAGBLUE"
        }) {
            HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap(checkThingType);
            for(String playerId : playerMap.keySet()) {
                gPlayer player = playerMap.get(playerId);
                //check null fields
                if(!player.containsFields(new String[]{"coordx", "coordy"}))
                    break;
                for (String propId : thingMap.keySet()) {
                    gProp prop = (gProp) thingMap.get(propId);
                    if(player.willCollideWithPropAtCoords(prop, player.getInt("coordx"),
                            player.getInt("coordy"))) {
                        prop.propEffect(player);
                    }
                }
            }
        }
        //check for winlose
        if(sSettings.net_server && cVars.isZero("gamewon")) {
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
                    if(cVars.isOne("gameteam"))
                        xCon.ex("say " + sVars.get("playercolor") + " team wins!");
                    else
                        xCon.ex("say " + sVars.get("playername") + " wins!");
                    cScoreboard.incrementScoreFieldById("server", "wins");
                }
                else {
                    //someone beats score
                    String highestId = cScoreboard.getWinnerId();
                    if(highestId.length() > 0) {
                        cVars.put("winnerid", highestId);
                        if(cVars.isOne("gameteam"))
                            xCon.ex("say "
                                    + nServer.instance().clientArgsMap.get(cVars.get("winnerid")).get("color") + " team wins!");
                        else
                            xCon.ex("say "
                                    + nServer.instance().clientArgsMap.get(cVars.get("winnerid")).get("name") + " wins!");
                        if(sSettings.net_server) {
                            cScoreboard.incrementScoreFieldById(cVars.get("winnerid"), "wins");
                        }
                    }
                }
                int toplay = (int) (Math.random() * eManager.winClipSelection.length);
                nServer.instance().addNetCmd("playsound sounds/win/"+eManager.winClipSelection[toplay]);
                cScripts.goToEndScreen();
            }
        }
    }
}
