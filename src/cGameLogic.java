import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class cGameLogic {

    static gPlayer userPlayer;

    public static gPlayer getUserPlayer() {
        if(userPlayer == null)
            userPlayer = getPlayerByIndex(0);
        return userPlayer;
    }

    public static gPlayer getPlayerByIndex(int n) {
        return eManager.currentMap.scene.players().size() > n ?
            eManager.currentMap.scene.players().get(n) :
            null;
    }

    public static gPlayer getPlayerById(String id) {
        for(gPlayer p : eManager.currentMap.scene.players()) {
            if(p.get("id").equals(id))
                return p;
        }
        return null;
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
            if(getUserPlayer() != null) {
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
            for(gPlayer p : eManager.currentMap.scene.players()) {
                if(p.get("id").contains("bot")) {
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
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
            if(cVars.isZero("inboost") && cVars.isOne("clipplayer")) {
                if (cVars.isOne("jumping")) {
                    xCon.ex("THING_PLAYER.0.mov1 0");
                    xCon.ex("THING_PLAYER.0.mov0 1");
                } else {
                    if(!cVars.contains("respawntime"))
                        xCon.ex("THING_PLAYER.0.mov1 1");
                    if (!getUserPlayer().canJump())
                        cVars.increment("falltime");
                    else
                        cVars.put("falltime", "0");
                    cVars.put("jumpheight", "0");
                }
            }
            //jumping
            if(cVars.isZero("inboost") && cVars.isOne("clipplayer")) {
                if(cVars.isOne("jumping") && cVars.getInt("jumpheight") < cVars.getInt("jumptimemax")) {
                    cVars.increment("jumpheight");
                    if(cVars.getInt("jumpheight") > cVars.getInt("jumpsquish"))
                        xCon.ex("-crouch");
                }
                else if(cVars.isOne("jumping")) {
                    xCon.ex("THING_PLAYER.0.mov0 0");
                    xCon.ex("cv_jumping 0");
                }
            }
            cVars.put("onladder", "0");
            cVars.put("inboost", "0");
        }
    }

    public static boolean drawSpawnProtection() {
        return cVars.contains("spawnprotectiontime")
                && cVars.getLong("spawnprotectiontime") > System.currentTimeMillis();
    }

    public static void resetGameState() {
        //TODO: keep wins when loading new map
        HashMap<String, Integer> savedWins = new HashMap<>();
        for(String id : nServer.scoresMap.keySet()) {
            savedWins.put(id, nServer.scoresMap.get(id).get("wins"));
        }
        nServer.scoresMap = new HashMap<>();
        if(sSettings.net_server) {
            nServer.scoresMap.put("server", new HashMap<>());
            nServer.scoresMap.get("server").put("wins", 0);
            nServer.scoresMap.get("server").put("score", 0);
            nServer.scoresMap.get("server").put("kills", 0);
            nServer.scoresMap.get("server").put("ping", 0);
            for(String id : savedWins.keySet()) {
                if(!nServer.scoresMap.containsKey(id)) {
                    nServer.scoresMap.put(id, new HashMap<>());
                    nServer.scoresMap.get(id).put("wins", 0);
                    nServer.scoresMap.get(id).put("score", 0);
                    nServer.scoresMap.get(id).put("kills", 0);
                    nServer.scoresMap.get(id).put("ping", 0);
                }
                nServer.scoresMap.get(id).put("wins", savedWins.get(id));
            }
        }
        cVars.put("gamewon", "0");
        cVars.put("winnerid","");
        if(cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
            || cVars.isInt("gamemode", cGameMode.FLAG_MASTER)) {
            cVars.put("flagmasterid", "");
        }
        if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS) {
            cGameMode.resetKingOfFlags();
        }
        if(cVars.getInt("gamemode") == cGameMode.SAFE_ZONES) {
            refreshSafeZones();
        }
        if(cVars.getInt("gamemode") == cGameMode.VIRUS) {
            refreshVirusPlayers();
        }
    }

    public static void checkMovementStatus() {
        //other players
        for(String id : nServer.clientArgsMap.keySet()) {
            for(gPlayer p : eManager.currentMap.scene.players()) {
                if(!id.equals(uiInterface.uuid) && p.get("id").equals(id) && nServer.clientArgsMap.containsKey(id)) {
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
        //ball prop
//        HashMap<String, gThing> props = eManager.currentMap.scene.getThingMap("PROP_BALLBOUNCY");
//        for(String id : props.keySet()) {
//            gProp p = (gProp) props.get(id);
//            cVars.putInt("ballx", p.getInt("coordx"));
//            cVars.putInt("bally", p.getInt("coordy"));
//        }
    }

    public static void checkDisconnectStatus() {
        if(sSettings.net_client && cVars.isOne("disconnectconfirmed")) {
            cVars.put("disconnecting", "0");
            uiInterface.clientSocket.close();
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
                for(gPlayer p : eManager.currentMap.scene.players()) {
                    if(!id.equals(uiInterface.uuid) && p.get("id").equals(id)) {
                        //check currentTime vs last recorded checkin time
                        long ctime = Long.parseLong(nServer.clientArgsMap.get(id).get("time"));
                        if(System.currentTimeMillis() > ctime + sVars.getInt("timeout")) {
                            nServer.quitClientIds.add(id);
                        }
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
        if(!xCon.ex("THING_PLAYER.0.name").equals(sVars.get("playername"))) {
            xCon.ex("THING_PLAYER.0.name playername"); //MARKER MAY BRAKE
        }
        //other players
        for(String id : nServer.clientArgsMap.keySet()) {
            for(gPlayer p : eManager.currentMap.scene.players()) {
                if(!id.equals(uiInterface.uuid) && p.get("id").equals(id)) {
                    String cname = nServer.clientArgsMap.get(id).get("name");
                    if(!p.get("name").equals(cname)) {
                        p.put("name", cname);
                    }
                }
            }
        }
    }

    public static void checkWeaponsStatus() {
        //player0
        if(cVars.getInt("currentweapon") != getUserPlayer().getInt("weapon")) {
            cScripts.changeWeapon(cVars.getInt("currentweapon"));
        }
        //pistol
        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.PISTOL.code()) < 1
            && cVars.getLong("weapontime" + gWeapons.type.PISTOL.code()) + cVars.getInt("delayweap")
                < System.currentTimeMillis()) {
            xCon.ex("playsound sounds/clampdown.wav");
            cVars.putInt("weaponstock"+ gWeapons.type.PISTOL.code(),
                    gWeapons.get(gWeapons.type.PISTOL).maxAmmo);
            cVars.put("reloading", "0");
        }
        //shotgun
        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.SHOTGUN.code()) < 1
            && cVars.getLong("weapontime" + gWeapons.type.SHOTGUN.code()) + cVars.getInt("delayweap")
                < System.currentTimeMillis()) {
            xCon.ex("playsound sounds/clampdown.wav");
            cVars.putInt("weaponstock"+ gWeapons.type.SHOTGUN.code(),
                    gWeapons.get(gWeapons.type.SHOTGUN).maxAmmo);
            cVars.put("reloading", "0");
        }
        //autorifle
        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.AUTORIFLE.code()) < 1
            && cVars.getLong("weapontime" + gWeapons.type.AUTORIFLE.code()) + cVars.getInt("delayweap")
                < System.currentTimeMillis()) {
            xCon.ex("playsound sounds/clampdown.wav");
            cVars.putInt("weaponstock"+ gWeapons.type.AUTORIFLE.code(),
                    gWeapons.get(gWeapons.type.AUTORIFLE).maxAmmo);
            cVars.put("reloading", "0");
        }
        //grenade
        if(cVars.isOne("allowweaponreload") && cVars.getInt("weaponstock" + gWeapons.type.LAUNCHER.code()) < 1
                && cVars.getLong("weapontime" + gWeapons.type.LAUNCHER.code()) + cVars.getInt("delayweap")
                < System.currentTimeMillis()) {
            xCon.ex("playsound sounds/clampdown.wav");
            cVars.putInt("weaponstock"+ gWeapons.type.LAUNCHER.code(),
                    gWeapons.get(gWeapons.type.LAUNCHER).maxAmmo);
            cVars.put("reloading", "0");
        }
        //other players
        for(String id : nServer.clientArgsMap.keySet()) {
            for(gPlayer p : eManager.currentMap.scene.players()) {
                if(p.get("id").equals(id) && !id.equals(uiInterface.uuid)) {
                    int cweap = Integer.parseInt(nServer.clientArgsMap.get(id).get("weapon"));
                    if(!p.isInt("weapon", cweap))
                        p.putInt("weapon", cweap);
                }
            }
        }
    }

    public static void checkHatStatus(){
        if(!xCon.ex("THING_PLAYER.0.pathspritehat").contains(sVars.get("playerhat"))) {
            cGameLogic.getUserPlayer().setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",
                    sVars.get("playerhat")))
            );
        }
        for(String id : nServer.clientArgsMap.keySet()) {
            for(gPlayer p : eManager.currentMap.scene.players()) {
                if(p.get("id").equals(id) && !id.equals(uiInterface.uuid)) {
                    String chat = nServer.clientArgsMap.get(id).get("hat");
                    if(!p.get("pathspritehat").contains(chat)) {
                        p.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",chat)));
                    }
                }
            }
        }
    }

    public static void checkColorStatus(){
        if(!xCon.ex("THING_PLAYER.0.color").contains(sVars.get("playercolor"))) {
            xCon.ex("THING_PLAYER.0.color playercolor");
            cGameLogic.getUserPlayer().setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s",
                    sVars.get("playercolor"),
                    xCon.ex("THING_PLAYER.0.pathsprite").substring(
                            xCon.ex("THING_PLAYER.0.pathsprite").lastIndexOf('/')))));
        }
        for(String id : nServer.clientArgsMap.keySet()) {
            for(gPlayer p : eManager.currentMap.scene.players()) {
                if(p.get("id").equals(id) && !id.equals(uiInterface.uuid)) {
                    String ccol = nServer.clientArgsMap.get(id).get("color");
                    if(!p.get("color").contains(ccol) || !p.get("pathsprite").contains(ccol)) {
                        p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", ccol,
                            p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
                        p.put("color", ccol);
                    }
                }
            }
        }
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
            for(gPlayer p : eManager.currentMap.scene.botplayers()) {
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
                cVars.put("speedbonus", "0");
                cVars.put("stockspeed", "0");
            }
            else {
                cVars.putInt("stockspeed", (int)(cVars.getLong("sprinttime")-System.currentTimeMillis()));
                cVars.putInt("speedbonus", (int)((double)cVars.getInt("velocityplayer")
                        *((double)cVars.getInt("stockspeed")/(double)cVars.getInt("maxstockspeed"))));
            }
        }
        else if(cVars.getInt("stockspeed") < 1) {
            cVars.put("sprint", "0");
            cVars.put("speedbonus", "0");
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
        if (eManager.currentMap.scene.players().size() > 0) {
            if(iMouse.holdingMouseLeft) {
                xCon.ex("attack");
            }
            else {
                cVars.put("firing", "0");
            }
            for(int i = 1; i < eManager.currentMap.scene.players().size(); i++) {
                gPlayer p = eManager.currentMap.scene.players().get(i);
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
                    nServer.incrementScoreFieldById("server", "kills");
                    xCon.ex("say " + sVars.get("playername") + " killed " + packName);
                    if(gamemode == cGameMode.DEATHMATCH) {
                        xCon.ex("givepoint " + cGameLogic.getUserPlayer().get("id"));
                    }
                    if((gamemode == cGameMode.CHOSENONE || gamemode == cGameMode.ANTI_CHOSENONE)
                            && cVars.get("chosenoneid").equals(packId)) {
                        if(gamemode == cGameMode.CHOSENONE) {
                            xCon.ex("cv_chosenoneid server");
                            xCon.ex("say " + xCon.ex("THING_PLAYER.0.name") + " is the chosen one!");
                        }
                        else {
                            xCon.ex("givepoint " + cGameLogic.getUserPlayer().get("id"));
                        }
                    }
                    if(gamemode == cGameMode.ANTI_CHOSENONE && cVars.isVal("chosenoneid", "server")) {
                        xCon.ex("givepoint " + cGameLogic.getUserPlayer().get("id"));
                        cVars.put("chosenoneid", packId);
                    }
                }
                else {
                    String killerid = action.replace("killedby", "");
                    if(!killerid.equals("God")) {
                        nServer.incrementScoreFieldById(killerid, "kills");
                        if (gamemode == cGameMode.DEATHMATCH)
                            xCon.ex("givepoint " + killerid);
                        xCon.ex("say " + cGameLogic.getPlayerById(killerid).get("name") + " killed " + packName);
                        if ((gamemode == cGameMode.CHOSENONE || gamemode == cGameMode.ANTI_CHOSENONE)
                                && cVars.get("chosenoneid").equals(packId)) {
                            if (gamemode == cGameMode.CHOSENONE) {
                                cVars.put("chosenoneid", killerid);
                                xCon.ex("say " + cGameLogic.getPlayerById(killerid).get("name")
                                        + " is the chosen one!");
                            } else {
                                xCon.ex("givepoint " + killerid);
                            }
                        }
                        if (gamemode == cGameMode.ANTI_CHOSENONE && cVars.isVal("chosenoneid", killerid)) {
                            xCon.ex("givepoint " + killerid);
                            cVars.put("chosenoneid", packId);
                        }
                    }
                    else {
                        //handle God/Guardians/Map kills separate
                        xCon.ex("say " + packName + " died");
                    }
                }
            }
            if(action.contains("explode")) {
                String[] args = action.split(":");
                if(sVars.isOne("vfxenableanimations"))
                    eManager.currentMap.scene.animations().add(
                            new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG, Integer.parseInt(args[1]),
                                    Integer.parseInt(args[2])));
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
                if(sVars.isOne("vfxenableanimations"))
                    eManager.currentMap.scene.animations().add(
                        new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG, Integer.parseInt(args[1]),
                                Integer.parseInt(args[2])));
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

    public static void refreshVirusPlayers() {
        String[] tmp = new String[eManager.currentMap.scene.players().size()];
        int i = 0;
        while(i < tmp.length) {
            tmp[i] = "0";
            i++;
        }
        tmp[(int)(Math.random()*(double)(tmp.length))] = "1"; //random player
        cVars.putArray("virustags", tmp);
    }

    public static void refreshSafeZones() {
        int canpass = 0;
        int i = 0;
        int[] proptags = new int[]{};
        for(gPropScorepoint safezone : eManager.currentMap.scene.scorepoints()) {
            safezone.put("int0", "0");
            canpass = 1;
            int[] tmp = Arrays.copyOf(proptags, proptags.length+1);
            tmp[tmp.length-1] = i;
            proptags = tmp;
            i++;
        }
        if(canpass > 0) {
            int rando = (int)(Math.random()*(double)(proptags.length));
            eManager.currentMap.scene.scorepoints().get(rando).putInt("int0", 1);
        }
    }

    public static void refreshWaypoints() {
        int pass = 1;
        int c = -1;
        int[] scorepoint_indexes = new int[]{};
        for(gProp p : eManager.currentMap.scene.scorepoints()) {
            c++;
            int[] tmp = Arrays.copyOf(scorepoint_indexes, scorepoint_indexes.length+1);
            tmp[tmp.length-1] = c;
            scorepoint_indexes = tmp;
            if(p.getInt("int0") > 0)
                pass = 0;
        }
        if(pass > 0) {
            int r = (int) (Math.random() * (scorepoint_indexes.length));
            for (int i = 0; i < eManager.currentMap.scene.scorepoints().size(); i++) {
                gPropScorepoint p = eManager.currentMap.scene.scorepoints().get(i);
                if (i == scorepoint_indexes[r])
                    p.put("int0", "1");
                else
                    p.put("int0", "0");
            }
        }
    }

    public static void checkVirus() {
        if(sSettings.net_server) {
            if(cVars.getLong("virustime") < uiInterface.gameTime) {
                if(nServer.clientArgsMap.containsKey("server")) {
                    for(gPlayer p : eManager.currentMap.scene.players()) {
                        if(nServer.clientArgsMap.get("server").containsKey("state")
                                && !nServer.clientArgsMap.get("server").get("state").contains(p.get("id"))
                                && p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000){
                            xCon.ex("givepoint " + p.get("id"));
                        }
                    }
                }
                cVars.putLong("virustime", uiInterface.gameTime + 1000);
            }
        }
    }

    public static void checkVirusSingle() {
        if(sSettings.net_server) {
            if(cVars.getLong("virussingletime") < uiInterface.gameTime) {
                boolean novirus = true;
                for(gPlayer p : eManager.currentMap.scene.players()) {
                    if(p.get("id").equals(cVars.get("virussingleid"))) {
                        novirus = false;
                    }
                    else if(p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000){
                        xCon.ex("givepoint " + p.get("id"));
                    }
                }
                if(novirus) {
                    assignRandomIdToCvar("virussingleid");
                }
                cVars.putLong("virussingletime", uiInterface.gameTime + 1000);
            }
            //check intersections
            for(gPlayer p1 : eManager.currentMap.scene.players()) {
                for(gPlayer p2 : eManager.currentMap.scene.players()) {
                    if(p1.get("id").equals(cVars.get("virussingleid"))
                    && cVars.getLong("virussingletagbacktime") < uiInterface.gameTime
                    && !p1.get("id").equals(p2.get("id"))
                    && new Rectangle(
                            p1.getInt("coordx"), p1.getInt("coordy"), p1.getInt("dimw"), p1.getInt("dimh")
                    ).intersects(new Rectangle(
                            p2.getInt("coordx"), p2.getInt("coordy"), p2.getInt("dimw"), p2.getInt("dimh"))
                    )) {
                        cVars.putLong("virussingletagbacktime",
                                uiInterface.gameTime + cVars.getInt("virussingletagbackwaittime"));
                        cVars.put("virussingleid", p2.get("id"));
                    }
                }
            }
        }
    }

    public static void checkFlagMaster() {
        if(sSettings.net_server) {
            if(cVars.getLong("flagmastertime") < uiInterface.gameTime) {
                for (gPlayer p : eManager.currentMap.scene.players()) {
                    if(p.get("id").equals(cVars.get("flagmasterid"))) {
                        xCon.ex("givepoint " + p.get("id"));
                    }
                }
                cVars.putLong("flagmastertime", uiInterface.gameTime + 1000);
            }
        }
    }

    public static void assignRandomIdToCvar(String cvar) {
        int r = (int) (Math.random()*((double)eManager.currentMap.scene.players().size()-1));
        gPlayer rp = cGameLogic.getPlayerByIndex(r);
        if(rp != null) {
            cVars.put(cvar, eManager.currentMap.scene.players().size() < 2 ? "server" : rp.get("id"));
        }
    }

    public static void checkChosenOne() {
        if(sSettings.net_server) {
            if(cVars.getLong("chosenonetime") < uiInterface.gameTime) {
                boolean nogolden = true;
                for(gPlayer p : eManager.currentMap.scene.players()) {
                    if(p.get("id").equals(cVars.get("chosenoneid"))) {
                        nogolden = false;
                        xCon.ex("givepoint " + p.get("id"));
                    }
                }
                if(nogolden) {
                    assignRandomIdToCvar("chosenoneid");
                }
                cVars.putLong("chosenonetime", uiInterface.gameTime + 1000);
            }
        }
    }

    public static void checkAntiChosenOne() {
        if(sSettings.net_server) {
            if(cVars.isVal("chosenoneid", "")) {
                assignRandomIdToCvar("chosenoneid");
            }
        }
    }

    public static void checkProp(gProp prop) {
        if(cGameLogic.userPlayer.willCollideWithPropAtCoords(prop,
                cGameLogic.userPlayer.getInt("coordx"), cGameLogic.userPlayer.getInt("coordy"))) {
            prop.propEffect(cGameLogic.userPlayer);
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
                    refreshWaypoints();
                    break;
                case cGameMode.VIRUS:
                    checkVirus();
                    break;
                case cGameMode.VIRUS_SINGLE:
                    checkVirusSingle();
                    break;
                case cGameMode.FLAG_MASTER:
                    checkFlagMaster();
                    break;
                case cGameMode.CHOSENONE:
                    checkChosenOne();
                    break;
                case cGameMode.ANTI_CHOSENONE:
                    checkAntiChosenOne();
                    break;
                default:
                    break;
            }
        }
        //check ALL PROPS this is the best one
        for(String checkThingType : new String[]{
                "PROP_TELEPORTER", "PROP_BOOSTUP", "PROP_POWERUP", "PROP_SCOREPOINT", "PROP_FLAGRED", "PROP_FLAGBLUE"
        }) {
            HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap(checkThingType);
            for(String id : thingMap.keySet()) {
                checkProp((gProp) thingMap.get(id));
            }
        }
//        //check new props scorepoints
//        for(gPropScorepoint sp : eManager.currentMap.scene.scorepoints()) {
//            checkProp((gPropScorepoint) sp);
//        }
//        //check new props flagsblue
//        for(gPropFlagBlue fb : eManager.currentMap.scene.flagsblue()) {
//            checkProp((gPropFlagBlue) fb);
//        }
//        //check new props flagsred
//        for(gPropFlagRed fr : eManager.currentMap.scene.flagsred()) {
//            checkProp((gPropFlagRed) fr);
//        }
        //old props
        for(gPlayer cl : eManager.currentMap.scene.players()) {
            for(gProp p : eManager.currentMap.scene.props()) {
                if(cl.willCollideWithPropAtCoords(p, cl.getInt("coordx"), cl.getInt("coordy"))) {
                    if(p.isInt("code", gProp.TELEPORTER) && cl.get("id").contains("bot")) {
                        //bot touches teleporter
                        p.propEffect(cl);
                    }
                    else if(sSettings.net_server && p.isInt("code", gProp.SCOREPOINT)
                            && cl.get("id").contains("bot")) {
                        //bot touches scorepoint
                        cScripts.checkPlayerScorepoints(p, cl);
                    }
                    else if(p.isInt("code", gProp.POWERUP)) {
                        if(p.getInt("int0") > 0) {
                            if(sSettings.net_server) {
                                if(cl.get("id").contains("bot")
                                        && cl.getLong("powerupsusetime") < System.currentTimeMillis()) {
                                    //do powerup effect
//                                    xCon.ex("say " + cl.get("name") + " picked up the "
//                                            + gWeapons.weapons_selection[p.getInt("int0")].name + "!");
                                        cl.putLong("powerupsusetime",
                                                System.currentTimeMillis()+sVars.getLong("powerupsusetimemax"));
                                    cScripts.changeBotWeapon(cl, p.getInt("int0"), true);
                                    p.put("int0", "0");
                                }
//                                else if(!cl.get("id").equals("server")
//                                        && cl.getInt("weapon") == gWeapons.Type.NONE.code()) {
//                                    //this is to hide a prop that client just picked up from unarmed pose
////                                    p.put("int0", "0");
//                                }
                            }
                        }
                    }
                    else if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS
                            && p.isInt("code", gProp.FLAGRED) && !p.isVal("str0", cl.get("id")) ) {
                        //handle kingofflag flagred intersection
                        int pass = 1;
                        for(gPlayer p2 : eManager.currentMap.scene.players()) {
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
                            && p.isInt("code", gProp.FLAGRED)
                            && cVars.isVal("flagmasterid", "")){
                        if(sSettings.net_server) {
                            xCon.ex("say " + cl.get("name") + " has the flag!");
                        }
                        cVars.put("flagmasterid", cl.get("id"));
                    }
                    else if(cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                            && p.isInt("code", gProp.FLAGBLUE)
                            && cVars.isVal("flagmasterid", cl.get("id"))){
                        if(sSettings.net_server) {
                            xCon.ex("givepoint " + cl.get("id"));
                            cVars.put("flagmasterid", "");
                            xCon.ex("say " + cl.get("name") + " captured the flag!");
                        }
                    }
                    else if(cVars.getInt("gamemode") == cGameMode.WAYPOINTS
                            && p.isInt("code", gProp.SCOREPOINT) && p.getInt("int0") > 0) {
                        if(sSettings.net_server) {
                            xCon.ex("givepoint " + cl.get("id"));
                            p.put("int0", "0");
                        }
                    }
                    else if(p.isInt("code", gProp.BALLBOUNCY)) {
                        if(sSettings.net_server) {
                            //clients interact with the ball depending on server-side coords
                            p.bounceOffPlayerBounds(cl);
                        }
                    }
                }
                else if(cl.isZero("tag") && p.isInt("code", gProp.TELEPORTER)
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
            || (cScripts.getWinnerScore() >= cVars.getInt("scorelimit"))) {
                cVars.put("gamewon", "1");
            }
            if(cVars.isOne("gamewon")) {
                if(cScripts.isTopScore()) {
                    cVars.put("winnerid", "server");
                    if(cVars.isOne("gameteam"))
                        xCon.ex("say " + sVars.get("playercolor") + " team wins!");
                    else
                        xCon.ex("say " + sVars.get("playername") + " wins!");
                    nServer.incrementScoreFieldById("server", "wins");
                }
                else {
                    //someone beats score
                    String highestId = cScripts.getWinnerId();
                    if(highestId.length() > 0) {
                        cVars.put("winnerid", highestId);
                        if(cVars.isOne("gameteam"))
                            xCon.ex("say "
                                    + nServer.clientArgsMap.get(cVars.get("winnerid")).get("color") + " team wins!");
                        else
                            xCon.ex("say "
                                    + nServer.clientArgsMap.get(cVars.get("winnerid")).get("name") + " wins!");
                        if(sSettings.net_server) {
                            nServer.incrementScoreFieldById(cVars.get("winnerid"), "wins");
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
        if(cVars.getInt("gamemode") == cGameMode.SAFE_ZONES) {
            for(gProp p : eManager.currentMap.scene.scorepoints()) {
                if(p.isInt("int0", 1))
                    return String.format("safezone-%s-%s-", p.get("tag"), cVars.get("safezonetime"));
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.WAYPOINTS) {
            int c = 0;
            for(gProp p : eManager.currentMap.scene.scorepoints()) {
                if(p.getInt("int0") > 0)
                    break;
                c++;
            }
            return String.format("waypoints-%d-", c);
        }
//        if(cVars.getInt("gamemode") == cGameMode.BOUNCYBALL) {
//            int c = 0;
//            for(gProp p : eManager.currentMap.scene.ballbouncys()) {
//                if(p.getInt("int0") > 0)
//                    break;
//                c++;
//            }
//            return String.format("bouncyball-%d-", c);
//        }
        // KINGOFFLAGS
        if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS) {
            StringBuilder s = new StringBuilder();
            HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
            for(String id : thingMap.keySet()) {
                s.append(String.format("%s-%s:", id, thingMap.get(id).get("str0")));
            }
            return String.format("kingofflags%s", s);
        }
        // VIRUS
        if(cVars.getInt("gamemode") == cGameMode.VIRUS) {
            //check
            if(!cVars.contains("virustags"))
                cGameLogic.refreshVirusPlayers();
            String[] vts = cVars.getArray("virustags");
            //check if reset time
            if(cVars.contains("virusresettime") && cVars.getLong("virusresettime") < System.currentTimeMillis()) {
                cGameLogic.refreshVirusPlayers();
                cVars.remove("virusresettime");
            }
            //check if more players
            if(vts.length != eManager.currentMap.scene.players().size()) {
                String[] na = Arrays.copyOf(vts, eManager.currentMap.scene.players().size());
                for(int i = vts.length; i < na.length; i++) {
                    na[i] = "1";
                }
                cVars.putArray("virustags", na);
            }
            String virusIdsStr = cScripts.getVirusIdsString();
            //check intersections
            for(int i = 0; i < eManager.currentMap.scene.players().size(); i++) {
                for(int j = 0; j < eManager.currentMap.scene.players().size(); j++) {
                    if(i != j) {
                        gPlayer p = cGameLogic.getPlayerByIndex(i);
                        gPlayer pp = cGameLogic.getPlayerByIndex(j);
                        Rectangle r = new Rectangle(p.getInt("coordx"), p.getInt("coordy"),
                                p.getInt("dimw"), p.getInt("dimh"));
                        Rectangle rr = new Rectangle(pp.getInt("coordx"), pp.getInt("coordy"),
                                pp.getInt("dimw"), pp.getInt("dimh"));
                        if (r.intersects(rr) && (
                                (p.getInt("tag") == 0 && virusIdsStr.contains("server"))
                                        || (p.get("id").length() > 0 && virusIdsStr.contains(p.get("id")))
                                        || (pp.get("id").length() > 0 && virusIdsStr.contains(pp.get("id")))
                        )) {
                            String[] cvts = cVars.getArray("virustags");
                            if(!cvts[i].equals("1")) {
                                xCon.ex("say " + pp.get("name") + " infected " + p.get("name") + "!");
                            }
                            if(!cvts[j].equals("1")) {
                                xCon.ex("say " + p.get("name") + " infected " + pp.get("name") + "!");
                            }
                            cVars.putInArray("virustags", "1", i);
                            cVars.putInArray("virustags", "1", j);
                        }
                    }
                }
            }
            virusIdsStr = cScripts.getVirusIdsString();
            //send codes before ids
            StringBuilder virusTagsStr = new StringBuilder();
            for(String s : cVars.getArray("virustags")) {
                virusTagsStr.append(s);
            }
            //check if thing if full, begin countdown to reset if it is
            String[] cts = virusIdsStr.split("-");
            if(cts.length-1 == vts.length && !cVars.contains("virusresettime")) {
                cVars.putLong("virusresettime", System.currentTimeMillis()+cVars.getInt("virusresetwaittime"));
            }
            return String.format("virus-%s%s", virusTagsStr.toString(), virusIdsStr);
        }
        return "";
    }

    public static void checkForPlayerDeath() {
        gPlayer cl = cGameLogic.getUserPlayer();
        cScripts.checkBulletSplashes();
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW && cVars.isZero("inboost")){
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
                refreshSafeZones();
                if (cVars.isOne("survivesafezone")) {
                    cVars.put("sendsafezone", "1");
                    if(sSettings.net_server) {
                        xCon.ex("givepoint " + cl.get("id"));
                    }
                }
                else {
                    cVars.put("exploded", "0");
                    cVars.putInt("explodex", cl.getInt("coordx") - 75);
                    cVars.putInt("explodey", cl.getInt("coordy") - 75);
                    if (sVars.isOne("vfxenableanimations"))
                        eManager.currentMap.scene.animations().add(new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                cVars.getInt("explodex"), cVars.getInt("explodey")));
                    if(sSettings.net_server)
                        xCon.ex("say " + cl.get("name") + " died");
                    xCon.ex("respawn");
                }
            }
            else
                cVars.put("survivesafezone", "0");
        }
    }
}
