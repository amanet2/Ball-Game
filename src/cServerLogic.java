import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class cServerLogic {
    static gScene scene = new gScene();
    public static void gameLoop() {
        if(sVars.getInt("timelimit") > 0)
            cVars.putLong("timeleft",
                    sVars.getLong("timelimit") - (int) (uiInterface.gameTime - cVars.getLong("starttime")));
        else
            cVars.putLong("timeleft", -1);
        checkHealthStatus();
        checkForMapChange();
        checkGameState();
        updateEntityPositions();
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
        for(String id : getPlayerIds()) {
            //this shouldnt be needed, but when server user joins his own games, it is
            if(id.equals(uiInterface.uuid))
                continue;
            gPlayer obj = getPlayerById(id);
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
                    if(scene.getThingMap("ITEM_FLAG").size() > 0
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
                            for(String id : getPlayerIds()) {
                                gPlayer p = getPlayerById(id);
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
        HashMap<String, gThing> playerMap = scene.getThingMap("THING_PLAYER");
        for (String playerId : playerMap.keySet()) {
            gPlayer player = (gPlayer) playerMap.get(playerId);
            //check null fields
            if (!player.containsFields(new String[]{"coordx", "coordy"}))
                break;
            //check player teleporters
            int clearTeleporterFlag = 1;
            for(String checkType : scene.objectMaps.keySet()) {
                if(checkType.contains("ITEM_") && !checkType.equals("ITEM_SPAWNPOINT")) {
                    HashMap<String, gThing> thingMap = scene.getThingMap(checkType);
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

    public static gThing getRandomSpawnpoint() {
        int size = scene.getThingMap("ITEM_SPAWNPOINT").size();
        if(size > 0) {
            int randomSpawnpointIndex = new Random().nextInt(size);
            ArrayList<String> spawnpointids =
                    new ArrayList<>(scene.getThingMap("ITEM_SPAWNPOINT").keySet());
            String randomId = spawnpointids.get(randomSpawnpointIndex);
            return scene.getThingMap("ITEM_SPAWNPOINT").get(randomId);
        }
        return null;
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
        HashMap playersMap = scene.getThingMap("THING_PLAYER");
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
        HashMap botsMap = scene.getThingMap("THING_BOTPLAYER");
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
            gPlayer p = getPlayerById(id);
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
                gPlayer p = getPlayerById(id);
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
            nServer.instance().sendMap(id);
            xCon.ex("respawnnetplayer " + id);
        }
    }

    public static void updateEntityPositions() {
        for(String id : getPlayerIds()) {
            gPlayer obj = getPlayerById(id);
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "accelrate", "mov0", "mov1",
                    "mov2", "mov3"};
            //check null fields
            if(!obj.containsFields(requiredFields))
                break;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < System.currentTimeMillis()) {
                obj.putLong("acceltick", System.currentTimeMillis()+obj.getInt("accelrate"));
                for (int i = 0; i < 4; i++) {
                    //user player
                    if(cClientLogic.isUserPlayer(obj)) {
                        if (obj.getInt("mov"+i) > 0) {
                            obj.putInt("vel" + i, (Math.min(cVars.getInt("velocityplayer"),
                                    obj.getInt("vel" + i) + 1)));
                        }
                        else
                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
                    }
                }
            }
            if(dx != obj.getInt("coordx") && obj.wontClipOnMove(0,dx, scene)) {
                obj.putInt("coordx", dx);
            }
            if(dy != obj.getInt("coordy") && obj.wontClipOnMove(1,dy, scene)) {
                obj.putInt("coordy", dy);
            }
        }

        HashMap bulletsMap = scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet obj = (gBullet) bulletsMap.get(id);
            obj.putInt("coordx", obj.getInt("coordx")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }
        HashMap popupsMap = scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup obj = (gPopup) popupsMap.get(id);
            obj.put("coordx", Integer.toString(obj.getInt("coordx")
                    - (int) (cVars.getInt("velocitypopup")*Math.cos(obj.getDouble("fv")+Math.PI/2))));
            obj.put("coordy", Integer.toString(obj.getInt("coordy")
                    - (int) (cVars.getInt("velocitypopup")*Math.sin(obj.getDouble("fv")+Math.PI/2))));
        }
        checkBulletSplashes();
    }

    public static void checkBulletSplashes() {
        ArrayList bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap bulletsMap = scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet b = (gBullet) bulletsMap.get(id);
            if(System.currentTimeMillis()-b.getLong("timestamp") > b.getInt("ttl")){
                bulletsToRemoveIds.add(id);
//                if (sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1) {
//                    currentMap.scene.getThingMap("THING_ANIMATION").put(
//                            createId(), new gAnimationEmitter(b.getInt("anim"),
//                                    b.getInt("coordx"), b.getInt("coordy")));
//                }
                //grenade explosion
                if(b.isInt("src", gWeapons.type.LAUNCHER.code())) {
                    pseeds.add(b);
                }
                continue;
            }
            for(String playerId : getPlayerIds()) {
                gPlayer t = getPlayerById(playerId);
                if(t != null && t.containsFields(new String[]{"coordx", "coordy"})
                        && b.doesCollideWithPlayer(t) && !b.get("srcid").equals(playerId)) {
                    bulletsToRemovePlayerMap.put(t, b);
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gBullet pseed : pseeds)
                gWeaponsLauncher.createGrenadeExplosion(pseed);
        }
        for(Object bulletId : bulletsToRemoveIds) {
            scene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            cClientLogic.playPlayerDeathSound();
            createDamagePopup(p, bulletsToRemovePlayerMap.get(p));
        }
    }

    //call this everytime a bullet intersects a player
    public static void createDamagePopup(gPlayer dmgvictim, gBullet bullet) {
        //get shooter details
        String killerid = bullet.get("srcid");
        //calculate dmg
        int adjusteddmg = bullet.getInt("dmg") - (int)((double)bullet.getInt("dmg")/2
                *((Math.abs(System.currentTimeMillis() - bullet.getLong("timestamp")
        )/(double)bullet.getInt("ttl"))));
        //play animations on all clients
//        if(sVars.isOne("vfxenableanimations") && bullet.getInt("anim") > -1)
//            currentMap.scene.getThingMap("THING_ANIMATION").put(
//                    createId(), new gAnimationEmitter(gAnimations.ANIM_SPLASH_RED,
//                            bullet.getInt("coordx"), bullet.getInt("coordy")));
        scene.getThingMap("THING_BULLET").remove(bullet.get("id"));
        //handle damage serverside
        if(sSettings.IS_SERVER) {
            String cmdString = "damageplayer " + dmgvictim.get("id") + " " + adjusteddmg + " " + killerid;
            nServer.instance().addNetCmd("server", cmdString);
            nServer.instance().addExcludingNetCmd("server",
                    "spawnpopup " + dmgvictim.get("id") + " -" + adjusteddmg);
        }
    }

    public static Collection<String> getPlayerIds() {
        return scene.getThingMap("THING_PLAYER").keySet();
    }

    public static gPlayer getPlayerById(String id) {
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }
}
