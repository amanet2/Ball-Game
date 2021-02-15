import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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
            if(userPlayer() != null) {
                checkForMapChange();
                checkMapGravity();
                cScripts.pointPlayerAtMousePointer();
                checkQuitterStatus();
                checkMovementStatus();
                checkNameStatus();
                checkHatStatus();
                checkColorStatus();
//                checkWeaponsStatus();
                checkHealthStatus();
                checkSprintStatus();
                checkPowerupsStatus();
                checkGameState();
                checkPlayersFire();
                checkForPlayerDeath();
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
                if (cVars.isOne("jumping")) {
                    userPlayer.putInt("mov1", 0);
                    userPlayer.putInt("vel0", cVars.getInt("gravity"));
                }
                else {
                    if(!userPlayer.contains("respawntime")) {
                        if(userPlayer.isOne("crouch"))
                            userPlayer.subtractVal("vel1",
                                    userPlayer.getInt("vel1") > 1 ? 1 : 0);
                        else
                            userPlayer.addVal("vel1",
                                    userPlayer.getInt("vel1") < cVars.getInt("gravity")
                                            && cVars.getInt("gravity") > 0
                            ? 1 : 0);
                    }
                    if (!userPlayer().canJump())
                        cVars.increment("falltime");
                    else
                        cVars.put("falltime", "0");
                    cVars.put("jumpheight", "0");
                }
            }
            //jumping
            if(cVars.isOne("clipplayer")) {
                int jumpmax = cVars.isInt("mapview", gMap.MAP_SIDEVIEW) ? cVars.getInt("jumptimemax")
                        : cVars.getInt("jumptimemax")/4;
                if(cVars.isOne("jumping") && cVars.getInt("jumpheight") < jumpmax) {
                    cVars.increment("jumpheight");
                    if(cVars.getInt("jumpheight") > cVars.getInt("jumpsquish"))
                        xCon.ex("-crouch");
                }
                else if(cVars.isOne("jumping")) {
                    if(cVars.isInt("mapview", gMap.MAP_SIDEVIEW))
                        userPlayer().putInt("mov0", 0);
                    cVars.putInt("jumping", 0);
                }
            }
    }

    public static boolean drawSpawnProtection() {
        gPlayer userplayer = cGameLogic.userPlayer();
        return userplayer != null && (userplayer.contains("spawnprotectiontime")
                && userplayer.getLong("spawnprotectiontime") > System.currentTimeMillis());
    }

    public static void resetGameState() {
        cScoreboard.resetScoresMap();
        cVars.put("gamewon", "0");
        cVars.put("winnerid","");
        switch (cVars.getInt("gamemode")) {
            case cGameMode.CAPTURE_THE_FLAG:
            case cGameMode.FLAG_MASTER:
                cVars.put("flagmasterid", "");
                break;
            case cGameMode.KING_OF_FLAGS:
                cGameMode.resetKingOfFlags();
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
        for(String id : nServer.clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid) && nServer.clientArgsMap.containsKey(id)) {
                gPlayer p = gScene.getPlayerById(id);
                HashMap<String, String> cargs = nServer.clientArgsMap.get(id);
                String[] requiredFields = new String[]{"fv", "dirs", "crouch", "flashlight", "x", "y"};
                boolean skip = false;
                for(String rf : requiredFields) {
                    if(!cargs.containsKey(rf)) {
                        skip = true;
                        break;
                    }
                }
                if(skip)
                    break;
                double cfv = Double.parseDouble(cargs.get("fv"));
                char[] cmovedirs = cargs.get("dirs").toCharArray();
                int ccrouch = Integer.parseInt(cargs.get("crouch"));
                int cflashlight = Integer.parseInt(cargs.get("flashlight"));
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
                if(!p.isInt("crouch", ccrouch))
                    p.putInt("crouch", ccrouch);
                if(!p.isInt("flashlight", cflashlight))
                    p.putInt("flashlight", cflashlight);
            }
        }
    }

    public static void checkDisconnectStatus() {
        if(sSettings.net_client && cVars.isOne("disconnectconfirmed")) {
            cVars.put("disconnecting", "0");
            nClient.clientSocket.close();
            sSettings.net_client = false;
            xCon.ex("load " + sVars.get("defaultmap"));
            if (uiInterface.inplay)
                xCon.ex("pause");
        }
    }

    public static void checkQuitterStatus() {
        if(sSettings.net_server) {
            //other players
            for(String id : nServer.clientArgsMap.keySet()) {
                if(!id.equals(uiInterface.uuid)) {
                    //check currentTime vs last recorded checkin time
                    long lastrecordedtime = Long.parseLong(nServer.clientArgsMap.get(id).get("time"));
                    if(System.currentTimeMillis() > lastrecordedtime + sVars.getInt("timeout")) {
                        nServer.quitClientIds.add(id);
                    }
                }
            }
            while(nServer.quitClientIds.size() > 0) {
                String quitterId = nServer.quitClientIds.remove();
                nServer.removeNetClient(quitterId);
            }
        }
        if(sSettings.net_client) {
            checkDisconnectStatus();
        }
    }

    public static void checkNameStatus() {
        //player0
        gPlayer userPlayer = cGameLogic.userPlayer();
        if(!userPlayer.isVal("name", sVars.get("playername"))) {
            userPlayer.put("name", sVars.get("playername"));
        }
        //other players
        for(String id : nServer.clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = gScene.getPlayerById(id);
                String cname = nServer.clientArgsMap.get(id).get("name");
                if(!p.get("name").equals(cname)) {
                    p.put("name", cname);
                }
            }
        }
    }

    public static void checkHatStatus(){
        //player0
        gPlayer userPlayer = cGameLogic.userPlayer();
        if(!userPlayer.isVal("pathspritehat", sVars.get("playerhat"))) {
            userPlayer.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",
                    sVars.get("playerhat"))));
        }
        //others
        for(String id : nServer.clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = gScene.getPlayerById(id);
                String chat = nServer.clientArgsMap.get(id).get("hat");
                if(!p.get("pathspritehat").contains(chat)) {
                    p.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",chat)));
                }
            }
        }
    }

    public static void checkColorStatus(){
        //player0
        gPlayer userPlayer = cGameLogic.userPlayer();
        if(!userPlayer.isVal("color", sVars.get("playercolor"))) {
            userPlayer.put("color", sVars.get("playercolor"));
            userPlayer.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s",
                    sVars.get("playercolor"), userPlayer.get("pathsprite").substring(
                            userPlayer.get("pathsprite").lastIndexOf('/')))));
        }
        //others
        for(String id : nServer.clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = gScene.getPlayerById(id);
                String ccol = nServer.clientArgsMap.get(id).get("color");
                if(!p.get("color").contains(ccol) || !p.get("pathsprite").contains(ccol)) {
                    p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", ccol,
                            p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
                    p.put("color", ccol);
                }
            }
        }
    }

    public static void checkWeaponsStatus() {
        //other players
        for(String id : nServer.clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = gScene.getPlayerById(id);
                int cweap = Integer.parseInt(nServer.clientArgsMap.get(id).get("weapon"));
                if(!p.isInt("weapon", cweap))
                    p.putInt("weapon", cweap);
            }
        }
    }

    public static boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", sSettings.net_server ? "server" : uiInterface.uuid);
    }

    public static void checkHealthStatus() {
        if(userPlayer.contains("respawntime") && (userPlayer.getLong("respawntime") < System.currentTimeMillis()
        || cVars.get("winnerid").length() > 0 || cVars.getInt("timeleft") <= 0)) {
            xCon.ex("respawn");
        }
        if(userPlayer.contains("spawnprotectiontime")
                && userPlayer.getLong("spawnprotectiontime") < System.currentTimeMillis()) {
            userPlayer.remove("spawnprotectiontime");
        }
        HashMap playersMap = eManager.currentMap.scene.getThingMap("THING_PLAYER");
        for(Object id : playersMap.keySet()) {
            gPlayer p = (gPlayer) playersMap.get(id);
            if(p.contains("respawntime") && (p.getLong("respawntime") < System.currentTimeMillis()
                    || cVars.get("winnerid").length() > 0 || cVars.getInt("timeleft") <= 0)) {
                if(p.isBot())
                    xCon.ex("botrespawn " + p.get("bottag"));
                else
                    p.remove("respawntime");
            }
            if(p.contains("spawnprotectiontime")
                    && p.getLong("spawnprotectiontime") < System.currentTimeMillis()) {
                p.remove("spawnprotectiontime");
            }
            if(p.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                    p.getLong("hprechargetime")+cVars.getInt("delayhp") < System.currentTimeMillis()) {
                if(p.getInt("stockhp")+cVars.getInt("rechargehp") > cVars.getInt("maxstockhp"))
                    p.put("stockhp", cVars.get("maxstockhp"));
                else
                    p.putInt("stockhp", p.getInt("stockhp") + cVars.getInt("rechargehp"));
            }
        }
//        if(cVars.contains("shaketime") && cVars.getLong("shaketime") > System.currentTimeMillis()) {
//            cVars.putInt("cammode", gCamera.MODE_SHAKYPROCEEDING);
//        }
//        else if(cVars.contains("shaketime")) {
//            cVars.putInt("cammode", gCamera.MODE_TRACKING);
//            cVars.remove("shaketime");
//        }
    }

    public static void checkSprintStatus() {
        if(cVars.isZero("sprint") && cVars.getInt("stockspeed") < cVars.getInt("maxstockspeed") &&
            cVars.getLong("sprinttime")+cVars.getInt("delaypow") < System.currentTimeMillis()) {
            if(cVars.getInt("stockspeed")+cVars.getInt("rechargepow") > cVars.getInt("maxstockspeed"))
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
            int rand = (int)(Math.random()*eManager.mapsSelection.length);
            eManager.mapSelectionIndex = rand;
            xCon.ex("load " + eManager.mapsSelection[rand]);
            xCon.ex("respawn");
            if(sSettings.net_server) {
                nServer.addSendCmd("load "+eManager.currentMap.mapName+sVars.get("mapextension"));
                nServer.addSendCmd("respawn");
            }
        }
    }

    public static void checkPlayersFire() {
        if(cGameLogic.userPlayer() != null && iMouse.holdingMouseLeft)
                xCon.ex("attack");
    }

    public static String getActionLoad() {
        String actionload = "";
        if(sSettings.net_client && cVars.get("sendpowerup").length() > 0) {
            actionload += ("sendpowerup"+cVars.get("sendpowerup")+"|");
            cVars.put("sendpowerup","");
        }
        if(cVars.isZero("exploded"))
            actionload += String.format("explode:%s:%s|", cVars.get("explodex"), cVars.get("explodey"));
        if(actionload.length() > 0)
            actionload.substring(0,actionload.length()-1); //cutoff last separator
        return actionload;
    }

    public static void checkGameState() {
        if(sSettings.net_server) {
            if(!cVars.get("scorelimit").equals(sVars.get("scorelimit")))
                cVars.put("scorelimit", sVars.get("scorelimit"));
            if(!cVars.get("gametick").equals(sVars.get("gametick")))
                cVars.put("gametick", sVars.get("gametick"));
            if(!cVars.get("spawnprotectionmaxtime").equals(sVars.get("spawnprotectionmaxtime")))
                cVars.put("spawnprotectionmaxtime", sVars.get("spawnprotectionmaxtime"));

            switch (cVars.getInt("gamemode")) {
                case cGameMode.KING_OF_FLAGS:
                    cGameMode.checkKingOfFlags();
                    break;
                case cGameMode.WAYPOINTS:
                    cGameMode.checkWaypoints();
                    break;
                case cGameMode.VIRUS:
                    cGameMode.checkVirus();
                    break;
                case cGameMode.FLAG_MASTER:
                    cGameMode.checkFlagMaster();
                    break;
                default:
                    break;
            }
        }
        //check ALL PROPS this is the best one
        //new way of checkingProps
        for(String checkThingType : new String[]{
                "PROP_TELEPORTER", "PROP_BOOSTUP", "PROP_POWERUP", "PROP_SCOREPOINT", "PROP_FLAGRED", "PROP_FLAGBLUE"
        }) {
            HashMap<String, gPlayer> playerMap = eManager.currentMap.scene.playersMap();
            HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap(checkThingType);
            for(String playerId : playerMap.keySet()) {
                gPlayer player = playerMap.get(playerId);
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
            if((cVars.getInt("timeleft") < 1 && cVars.getLong("intermissiontime") < 0)
            || (cScoreboard.getWinnerScore() >= cVars.getInt("scorelimit"))) {
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
                                    + nServer.clientArgsMap.get(cVars.get("winnerid")).get("color") + " team wins!");
                        else
                            xCon.ex("say "
                                    + nServer.clientArgsMap.get(cVars.get("winnerid")).get("name") + " wins!");
                        if(sSettings.net_server) {
                            cScoreboard.incrementScoreFieldById(cVars.get("winnerid"), "wins");
                        }
                    }
                }
                int toplay = (int) (Math.random() * eManager.winClipSelection.length);
                nServer.addSendCmd("playsound sounds/win/"+eManager.winClipSelection[toplay]);
                cScripts.goToEndScreen();
            }
        }
    }

    public static void checkForPlayerDeath() {
        gPlayer cl = cGameLogic.userPlayer();
//        cScripts.checkBulletSplashes();
        if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW){
            if(cVars.getInt("falltime") > cVars.getInt("fallkilltime")
            && !cl.contains("respawntime")) {
                cScripts.playPlayerDeathSound();
                cl.put("stockhp", cVars.get("maxstockhp"));
                xCon.ex("respawn");
                cVars.put("falltime", "0");
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.SAFE_ZONES) {
            if(cVars.getLong("safezonetime") < 0) {
                cVars.putLong("safezonetime", System.currentTimeMillis() + sVars.getInt("safezonetime"));
            }
            if(cVars.getLong("safezonetime") < System.currentTimeMillis()) {
                cVars.putLong("safezonetime", System.currentTimeMillis() + sVars.getInt("safezonetime"));
                cGameMode.refreshSafeZones();
                cVars.put("exploded", "0");
                cVars.putInt("explodex", cl.getInt("coordx") - 75);
                cVars.putInt("explodey", cl.getInt("coordy") - 75);
                if (sVars.isOne("vfxenableanimations")) {
                    eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                            cScripts.createID(8), new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                            cVars.getInt("explodex"), cVars.getInt("explodey")));
                }
                if(sSettings.net_server)
                    xCon.ex("say " + cl.get("name") + " died");
                xCon.ex("respawn");
            }
        }
    }
}
