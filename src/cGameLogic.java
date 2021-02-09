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

    public static void damageHealth(int dmg) {
        cVars.putInt("stockhp", Math.max(cVars.getInt("stockhp") - dmg, 0));
        cVars.putLong("hprechargetime", System.currentTimeMillis());
    }

    public static void damageBotHealth(gPlayer bot, int dmg) {
        bot.putInt("stockhp", Math.max(bot.getInt("stockhp") - dmg, 0));
        bot.putLong("hprechargetime", System.currentTimeMillis());
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
                checkWeaponsStatus();
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
                        if (cVars.isZero("gamespawnarmed") && p.getInt("weapon") != gWeapons.type.NONE.code()) {
                            cScripts.changeBotWeapon(p, gWeapons.type.NONE.code(), true);
                        }
                    }
                }
            }
        }
    }

    public static void checkMapGravity() {
            if(cVars.isZero("inboost") && cVars.isOne("clipplayer")) {
                gPlayer userPlayer = cGameLogic.userPlayer();
                if (cVars.isOne("jumping")) {
                    userPlayer.putInt("mov1", 0);
                    userPlayer.putInt("vel0", cVars.getInt("gravity"));
                }
                else {
                    if(!cVars.contains("respawntime")) {
                        if(userPlayer.isOne("crouch"))
                            userPlayer.subtractVal("vel1",
                                    userPlayer.getInt("vel1") > 1 ? 1 : 0);
                        else
                            userPlayer.addVal("vel1",
                                    userPlayer.getInt("vel1") < cVars.getInt("gravity")
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
            if(cVars.isZero("inboost") && cVars.isOne("clipplayer")) {
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
            cVars.put("onladder", "0");
            cVars.put("inboost", "0");
    }

    public static boolean drawSpawnProtection() {
        return cVars.contains("spawnprotectiontime")
                && cVars.getLong("spawnprotectiontime") > System.currentTimeMillis();
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
                double cfv = Double.parseDouble(cargs.get("fv"));
                char[] cmovedirs = cargs.get("dirs").toCharArray();
                int ccrouch = Integer.parseInt(cargs.get("crouch"));
                int cfire = Integer.parseInt(cargs.get("fire"));
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
                if(!p.isInt("firing", cfire))
                    p.putInt("firing", cfire);
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
                    long ctime = Long.parseLong(nServer.clientArgsMap.get(id).get("time"));
                    if(System.currentTimeMillis() > ctime + sVars.getInt("timeout")) {
                        nServer.quitClientIds.add(id);
                    }
                }
            }
            while(nServer.quitClientIds.size() > 0) {
                String quitterId = nServer.quitClientIds.remove();
                nServer.removeNetClient(quitterId);
            }
            while(nServer.kickClientIds.size() > 0 && nServer.kickConfirmed) {
                nServer.kickClientIds.remove();
                nServer.kickConfirmed = false;
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
        //unused reloading code
//        //pistol
//        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.PISTOL.code()) < 1
//            && cVars.getLong("weapontime" + gWeapons.type.PISTOL.code()) + cVars.getInt("delayweap")
//                < System.currentTimeMillis()) {
//            xCon.ex("playsound sounds/clampdown.wav");
//            cVars.putInt("weaponstock"+ gWeapons.type.PISTOL.code(),
//                    gWeapons.get(gWeapons.type.PISTOL).maxAmmo);
//            cVars.put("reloading", "0");
//        }
//        //shotgun
//        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.SHOTGUN.code()) < 1
//            && cVars.getLong("weapontime" + gWeapons.type.SHOTGUN.code()) + cVars.getInt("delayweap")
//                < System.currentTimeMillis()) {
//            xCon.ex("playsound sounds/clampdown.wav");
//            cVars.putInt("weaponstock"+ gWeapons.type.SHOTGUN.code(),
//                    gWeapons.get(gWeapons.type.SHOTGUN).maxAmmo);
//            cVars.put("reloading", "0");
//        }
//        //autorifle
//        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.AUTORIFLE.code()) < 1
//            && cVars.getLong("weapontime" + gWeapons.type.AUTORIFLE.code()) + cVars.getInt("delayweap")
//                < System.currentTimeMillis()) {
//            xCon.ex("playsound sounds/clampdown.wav");
//            cVars.putInt("weaponstock"+ gWeapons.type.AUTORIFLE.code(),
//                    gWeapons.get(gWeapons.type.AUTORIFLE).maxAmmo);
//            cVars.put("reloading", "0");
//        }
//        //grenade
//        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.LAUNCHER.code()) < 1
//                && cVars.getLong("weapontime" + gWeapons.type.LAUNCHER.code()) + cVars.getInt("delayweap")
//                < System.currentTimeMillis()) {
//            xCon.ex("playsound sounds/clampdown.wav");
//            cVars.putInt("weaponstock"+ gWeapons.type.LAUNCHER.code(),
//                    gWeapons.get(gWeapons.type.LAUNCHER).maxAmmo);
//            cVars.put("reloading", "0");
//        }
    }

    public static boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", sSettings.net_server ? "server" : uiInterface.uuid);
    }

    public static void checkHealthStatus() {
        if(cVars.contains("respawntime") && (cVars.getLong("respawntime") < System.currentTimeMillis()
        || cVars.get("winnerid").length() > 0 || cVars.getInt("timeleft") <= 0)) {
            xCon.ex("respawn");
            cVars.remove("respawntime");
        }
        if(cVars.contains("spawnprotectiontime")
                && cVars.getLong("spawnprotectiontime") < System.currentTimeMillis()) {
            cVars.remove("spawnprotectiontime");
        }
        if(sSettings.net_server) {
            HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
            for(Object id : botsMap.keySet()) {
                gPlayer p = (gPlayer) botsMap.get(id);
                if(p.contains("respawntime") && (p.getLong("respawntime") < System.currentTimeMillis()
                        || cVars.get("winnerid").length() > 0 || cVars.getInt("timeleft") <= 0)) {
                    xCon.ex("botrespawn " + p.get("bottag"));
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
        }
        if(cVars.contains("shaketime") && cVars.getLong("shaketime") > System.currentTimeMillis()) {
            cVars.putInt("cammode", gCamera.MODE_SHAKYPROCEEDING);
        }
        else if(cVars.contains("shaketime")) {
            cVars.putInt("cammode", gCamera.MODE_TRACKING);
            cVars.remove("shaketime");
        }
        if(cVars.getInt("stockhp") < cVars.getInt("maxstockhp") &&
            cVars.getLong("hprechargetime")+cVars.getInt("delayhp") < System.currentTimeMillis()) {
            if(cVars.getInt("stockhp")+cVars.getInt("rechargehp") > cVars.getInt("maxstockhp"))
                cVars.put("stockhp", cVars.get("maxstockhp"));
            else
                cVars.putInt("stockhp", cVars.getInt("stockhp") + cVars.getInt("rechargehp"));
        }
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
        if((sSettings.net_server && cVars.getLong("intermissiontime") > 0 && cVars.getLong("intermissiontime") < System.currentTimeMillis())) {
            cVars.put("intermissiontime", "-1");
            cVars.putInt("timeleft", sVars.getInt("timelimit"));
            int rand = (int)(Math.random()*eManager.mapsSelection.length);
            while(rand == eManager.mapSelectionIndex) {
                rand = (int)(Math.random()*eManager.mapsSelection.length);
            }
            eManager.mapSelectionIndex = rand;
            xCon.ex("load " + eManager.mapsSelection[rand]);
            xCon.ex("respawn");
        }
    }

    public static void checkPlayersFire() {
        if (eManager.currentMap.scene.playersMap().size() > 0) {
            if(iMouse.holdingMouseLeft) {
                xCon.ex("attack");
            }
            else {
                cVars.put("firing", "0");
            }
            for(String id : gScene.getPlayerIds()) {
                gPlayer p = gScene.getPlayerById(id);
                if(p.isOne("firing") && p.getLong("cooldown") < System.currentTimeMillis()) {
                    p.fireWeapon();
                    p.putLong("cooldown",
                            (System.currentTimeMillis() + gWeapons.fromCode(p.getInt("weapon")).refiredelay));
                }
                if(p.isInt("crouch", 1)) {
                    xCon.ex("playercrouch " + p.get("id"));
                }
                else {
                    xCon.ex("-playercrouch " + p.get("id"));
                }
            }
        }
    }

    public static String getActionLoad() {
        String actionload = "";
        if(sSettings.net_client && cVars.isOne("sendsafezone"))
            actionload += "safezone|";
        if(sSettings.net_client && cVars.get("sendpowerup").length() > 0) {
            actionload += ("sendpowerup"+cVars.get("sendpowerup")+"|");
            cVars.put("sendpowerup","");
        }
        if(cVars.get("sendcmd").length() > 0) {
            actionload+=("sendcmd_"+cVars.get("sendcmd")+"|");
            cVars.put("sendcmd","");
        }
        if(sSettings.net_client && cVars.isOne("lapcomplete")) {
            actionload += "lapcomplete|";
            xCon.ex("cv_lapcomplete 0");
        }
        if(cVars.isZero("exploded"))
            actionload += String.format("explode:%s:%s|", cVars.get("explodex"), cVars.get("explodey"));
        if(cVars.isZero("reportedkiller")) {
            actionload += String.format("killedby%s|", cVars.get("killerid"));
            cVars.put("killerid", "God");
        }
        if(actionload.length() > 0)
            actionload.substring(0,actionload.length()-1); //cutoff last separator
        return actionload;
    }

    public static void processActionLoadServer(String packActions, String packName, String packId) {
        String[] actions = packActions.split("\\|");
        for(String action : actions) {
            if((action.contains("lapcomplete") && cVars.getInt("gamemode") == cGameMode.RACE)
                    || (action.contains("safezone") && cVars.getInt("gamemode") == cGameMode.SAFE_ZONES)
                    || (action.contains("waypoint") && cVars.getInt("gamemode") == cGameMode.WAYPOINTS)) {
                xCon.ex("givepoint " + packId);
            }
            if(action.contains("killedby")) {
                int gamemode = cVars.getInt("gamemode");
                if((gamemode == cGameMode.CAPTURE_THE_FLAG || gamemode == cGameMode.FLAG_MASTER)
                        && cVars.get("flagmasterid").equals(packId)) {
                    cVars.put("flagmasterid", "");
                    xCon.ex("say " + packName + " lost the flag!");
                }
                if (action.replace("killedby", "").equals("server")) {
                    cScoreboard.incrementScoreFieldById("server", "kills");
                    xCon.ex("say " + sVars.get("playername") + " killed " + packName);
                    if(gamemode == cGameMode.DEATHMATCH) {
                        xCon.ex("givepoint " + cGameLogic.userPlayer().get("id"));
                    }
                }
                else {
                    String killerid = action.replace("killedby", "");
                    if(!killerid.equals("God")) {
                        cScoreboard.incrementScoreFieldById(killerid, "kills");
                        if (gamemode == cGameMode.DEATHMATCH)
                            xCon.ex("givepoint " + killerid);
                        xCon.ex("say " + gScene.getPlayerById(killerid).get("name") + " killed " + packName);
                    }
                    else {
                        //handle God/Guardians/Map kills separate
                        xCon.ex("say " + packName + " died");
                    }
                }
            }
            if(action.contains("explode")) {
                String[] args = action.split(":");
                if (sVars.isOne("vfxenableanimations")) {
                    eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                            cScripts.createID(8), new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                    Integer.parseInt(args[1]), Integer.parseInt(args[2])));
                }
            }
            if(action.contains("sendpowerup")) {
                String[] sptoks = action.replace("sendpowerup", "").split(":");
                gProp p = (gProp) eManager.currentMap.scene.getThingMap("PROP_POWERUP").get(sptoks[0]);
                p.put("int1", sptoks[1]);
                if(Integer.parseInt(p.get("int1")) < 1)
                    p.put("int0","0");
            }
            if(action.contains("sendcmd")) {
                xCon.ex(action.replaceFirst("sendcmd_",""));
            }
        }
    }

    public static void processActionLoadClient(String actionload) {
        String[] actions = actionload.split("\\|");
        for(String action : actions) {
            if(action.contains("explode")) {
                String[] args = action.split(":");
                if (sVars.isOne("vfxenableanimations")) {
                    eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                            cScripts.createID(8), new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                    Integer.parseInt(args[1]), Integer.parseInt(args[2])));
                }
            }
            if(action.contains("playsound")) {
                nClient.sfxreceived = 1;
                xCon.ex(String.format("playsound %s",
                        action.split("-")[0].replace("playsound","")));
            }
            if(action.contains("sendcmd")) {
                nClient.cmdreceived = 1;
                xCon.ex(action.replaceFirst("sendcmd_",""));
            }
        }
    }

    public static void checkProp(gProp prop) {
        if(userPlayer().willCollideWithPropAtCoords(prop, userPlayer().getInt("coordx"),
                userPlayer().getInt("coordy"))) {
            prop.propEffect(userPlayer());
        }
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
            HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap(checkThingType);
            for(String id : thingMap.keySet()) {
                checkProp((gProp) thingMap.get(id));
            }
        }
        //old props, other players
        for(String id : gScene.getPlayerIds()) {
            gPlayer cl = gScene.getPlayerById(id);
            for(gProp p : eManager.currentMap.scene.props()) {
                if(cl.willCollideWithPropAtCoords(p, cl.getInt("coordx"), cl.getInt("coordy"))) {
                    if(p.isInt("code", gProps.TELEPORTER) && cl.get("id").contains("bot")) {
                        //bot touches teleporter
                        p.propEffect(cl);
                    }
                    else if(sSettings.net_server && p.isInt("code", gProps.SCOREPOINT)
                            && cl.get("id").contains("bot")) {
                        //bot touches scorepoint
                        cScripts.checkPlayerScorepoints(p, cl);
                    }
                    else if(p.isInt("code", gProps.POWERUP)) {
                        if(p.getInt("int0") > 0) {
                            if(sSettings.net_server) {
                                if(cl.get("id").contains("bot")
                                        && cl.getLong("powerupsusetime") < System.currentTimeMillis()) {
                                    //do powerup effect
                                        cl.putLong("powerupsusetime",
                                                System.currentTimeMillis()+sVars.getLong("powerupsusetimemax"));
                                    cScripts.changeBotWeapon(cl, p.getInt("int0"), true);
                                    p.put("int0", "0");
                                }
                            }
                        }
                    }
                    else if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS
                            && p.isInt("code", gProps.FLAGRED) && !p.isVal("str0", cl.get("id")) ) {
                        //handle kingofflag flagred intersection
                        int pass = 1;
                        for(String id2 : gScene.getPlayerIds()) {
                            gPlayer p2 = gScene.getPlayerById(id2);
                            //make sure no other players still on the flag
                            if(!p2.get("id").equals(cl.get("id"))
                                    && p2.willCollideWithPropAtCoords(p,
                                    p2.getInt("coordx"), p2.getInt("coordy"))) {
                                pass = 0;
                                break;
                            }
                        }
                        if(pass > 0) {
                            p.put("str0", cl.get("id"));
                            if (sSettings.net_server) {
                                xCon.ex("say " + cl.get("name") + " captured flag#" + p.getInt("tag"));
                                xCon.ex("givepoint " + cl.get("id"));
                            }
                        }
                    }
                    else if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                            || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                            && p.isInt("code", gProps.FLAGRED)
                            && cVars.isVal("flagmasterid", "")){
                        if(sSettings.net_server) {
                            xCon.ex("say " + cl.get("name") + " has the flag!");
                        }
                        cVars.put("flagmasterid", cl.get("id"));
                    }
                    else if(cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                            && p.isInt("code", gProps.FLAGBLUE)
                            && cVars.isVal("flagmasterid", cl.get("id"))){
                        if(sSettings.net_server) {
                            xCon.ex("givepoint " + cl.get("id"));
                            cVars.put("flagmasterid", "");
                            xCon.ex("say " + cl.get("name") + " captured the flag!");
                        }
                    }
                    else if(cVars.getInt("gamemode") == cGameMode.WAYPOINTS
                            && p.isInt("code", gProps.SCOREPOINT) && p.getInt("int0") > 0) {
                        if(sSettings.net_server) {
                            xCon.ex("givepoint " + cl.get("id"));
                            p.put("int0", "0");
                        }
                    }
                }
                else if(cl.isZero("tag") && p.isInt("code", gProps.TELEPORTER)
                        && p.isVal("tag", cVars.get("exitteleportertag"))) {
                    //not colliding with our exit teleporter now
                    cVars.put("exitteleportertag", "-1");
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
                cVars.put("sendsound", "sounds/win/"+eManager.winClipSelection[toplay]);
                cScripts.goToEndScreen();
            }
        }
    }

    public static String getGameStateServer() {
        //ugly if else
        HashMap thingMap;
        switch(cVars.getInt("gamemode")) {
            case cGameMode.SAFE_ZONES:
                thingMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                for(Object id : thingMap.keySet()) {
                    gProp p = (gProp) thingMap.get(id);
                    if(p.isInt("int0", 1))
                        return String.format("safezone-%s-%s", p.get("tag"), cVars.get("safezonetime"));
                }
                break;
            case cGameMode.WAYPOINTS:
                thingMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                for(Object id : thingMap.keySet()) {
                    gProp p = (gProp) thingMap.get(id);
                    if(p.isInt("int0", 1))
                        return String.format("waypoints-%s", id);
                }
                break;
            case cGameMode.KING_OF_FLAGS:
                StringBuilder s = new StringBuilder();
                HashMap<String, gThing> kofThingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                for(String id : kofThingMap.keySet()) {
                    s.append(String.format("%s-%s:", id, kofThingMap.get(id).get("str0")));
                }
                return String.format("kingofflags%s", s);
            case cGameMode.VIRUS:
                //check
                if(cVars.get("virusids").length() < 1)
                    cGameMode.resetVirusPlayers();
                //check if reset time
                if(cVars.contains("virusresettime")
                        && cVars.getLong("virusresettime") < System.currentTimeMillis()) {
                    cGameMode.resetVirusPlayers();
                    cVars.remove("virusresettime");
                }
                //check intersections
                for(String id1 : gScene.getPlayerIds()) {
                    for(String id2 : gScene.getPlayerIds()) {
                        if(!id1.equals(id2)) {
                            gPlayer p = gScene.getPlayerById(id1);
                            gPlayer pp = gScene.getPlayerById(id2);
                            Rectangle r = new Rectangle(p.getInt("coordx"), p.getInt("coordy"),
                                    p.getInt("dimw"), p.getInt("dimh"));
                            Rectangle rr = new Rectangle(pp.getInt("coordx"), pp.getInt("coordy"),
                                    pp.getInt("dimw"), pp.getInt("dimh"));
                            if (r.intersects(rr) && (
                                    (p.getInt("tag") == 0 && cVars.get("virusids").contains("server"))
                                            || (p.get("id").length() > 0 && cVars.get("virusids").contains(p.get("id")))
                                            || (pp.get("id").length() > 0 && cVars.get("virusids").contains(pp.get("id")))
                            )) {
                                if(!cVars.get("virusids").contains(id1)) {
                                    xCon.ex("say " + pp.get("name") + " infected " + p.get("name") + "!");
                                    cVars.put("virusids", cVars.get("virusids")+id1+"-");
                                }
                                if(!cVars.get("virusids").contains(id2)) {
                                    xCon.ex("say " + p.get("name") + " infected " + pp.get("name") + "!");
                                    cVars.put("virusids", cVars.get("virusids")+id2+"-");
                                }
                            }
                        }
                    }
                }
                //check if thing if full, begin countdown to reset if it is
                boolean isFull = true;
                for(String id : gScene.getPlayerIds()) {
                    if(!cVars.get("virusids").contains(id)) {
                        isFull = false;
                        break;
                    }
                }
                if (isFull) {
                    if(!cVars.contains("virusresettime")) {
                        cVars.putLong("virusresettime",
                                System.currentTimeMillis()+cVars.getInt("virusresetwaittime"));
                    }
                }
                return String.format("virus-%s", cVars.get("virusids"));
            default:
                break;
        }
        return "";
    }

    public static void checkForPlayerDeath() {
        gPlayer cl = cGameLogic.userPlayer();
        cScripts.checkBulletSplashes();
        if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW && cVars.isZero("inboost")){
            if(cVars.getInt("falltime") > cVars.getInt("fallkilltime")
            && !cVars.contains("respawntime")) {
                cScripts.playPlayerDeathSound();
                cVars.put("stockhp", cVars.get("maxstockhp"));
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
                if (cVars.isOne("survivesafezone")) {
                    cVars.put("sendsafezone", "1");
                    if(sSettings.net_server) {
                        xCon.ex("givepoint " + cl.get("id"));
                    }
                }
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
            else
                cVars.put("survivesafezone", "0");
        }
    }
}
