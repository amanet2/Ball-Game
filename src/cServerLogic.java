import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class cServerLogic {
    static gScene scene = new gScene();
    public static void gameLoop() {
        checkTimeRemaining();
        checkHealthStatus();
        checkForMapChange();
        checkGameState();
        updateEntityPositions();
        checkBulletSplashes();
    }

    public static String getRandomClientDmgString() {
        char[] selection = new char[]{'#', '$', '%', '^', '&', '*'};
        StringBuilder expr = new StringBuilder();
        expr.append(selection[(int) (Math.random() * selection.length)]);
        while(expr.length() < 4) {
            char rando = selection[(int)(Math.random()*selection.length)];
            while(rando == expr.charAt(expr.length()-1)) {
                rando = selection[(int) (Math.random() * selection.length)];
            }
            expr.append(rando);
        }
        return expr.append('!').toString();
    }

    public static void checkTimeRemaining() {
        if(sVars.getInt("timelimit") > 0)
            cVars.putLong("timeleft", Math.max(0, sVars.getLong("timelimit")
                    - (int) (uiInterface.gameTime - cVars.getLong("starttime"))));
        else
            cVars.putLong("timeleft", -1);
    }

    public static void checkGameState() {
        for(String id : getPlayerIds()) {
            //this is needed when server user joins his own games
            if(id.equals(uiInterface.uuid))
                continue;
            gPlayer obj = getPlayerById(id);
            for (int i = 0; i < 4; i++) {
                if(nServer.instance().clientArgsMap.get(obj.get("id")).containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(nServer.instance().clientArgsMap.get(
                            obj.get("id")).get("vels").split("-")[i]));
            }
        }
        if(nServer.instance().clientArgsMap.containsKey("server")) {
            if(nServer.instance().clientArgsMap.get("server").containsKey("flagmasterid")) {
                if(scene.getThingMap("ITEM_FLAG").size() > 0)
                    xCon.ex("clearthingmap ITEM_FLAG");
                if(cVars.getLong("flagmastertime") < uiInterface.gameTime) {
                    xCon.ex("givepoint " + nServer.instance().clientArgsMap.get("server").get("flagmasterid"));
                    cVars.putLong("flagmastertime", uiInterface.gameTime + 1000);
                }
            }
            if(nServer.instance().clientArgsMap.get("server").containsKey("virusids")) {
                if(cVars.getLong("virustime") < uiInterface.gameTime) {
                    boolean survivors = false;
                    for(String id : getPlayerIds()) {
                        if(!nServer.instance().clientArgsMap.get("server").get("virusids").contains(id)) {
                            survivors = true;
                            xCon.ex("givepoint " + id);
                        }
                    }
                    cVars.putLong("virustime", uiInterface.gameTime + 1000);
                    if(!survivors) {
                        cGameLogic.resetVirusPlayers();
                    }
                }
            }
            else if(cVars.isInt("gamemode", cGameLogic.VIRUS)) {
                cGameLogic.resetVirusPlayers();
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
        if(!sSettings.show_mapmaker_ui && cVars.isZero("gameover")) {
            //conditions
            if((cVars.getInt("timeleft") > -1 && cVars.getInt("timeleft") < 1
                    && cVars.getLong("intermissiontime") < 0)) {
                cVars.put("gameover", "1");
            }
            if(cVars.isOne("gameover")) {
                String highestId = gScoreboard.getWinnerId();
                if(highestId.length() > 0) {
                    gScoreboard.incrementScoreFieldById(highestId, "wins");
                    nServer.instance().addExcludingNetCmd("server", "echo "
                            + nServer.instance().clientArgsMap.get(highestId).get("name") + " wins");
                }
                int toplay = (int) (Math.random() * eManager.winClipSelection.length);
                nServer.instance().addExcludingNetCmd("server",
                        "playsound sounds/win/"+eManager.winClipSelection[toplay]);
                cVars.putLong("intermissiontime",
                        System.currentTimeMillis() + Integer.parseInt(sVars.get("intermissiontime")));
                nServer.instance().addExcludingNetCmd("server",
                        "echo changing map...");
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
//        //player0
//        gPlayer userPlayer = cClientLogic.getUserPlayer();
//        if(userPlayer != null && !userPlayer.get("pathspritehat").contains(sVars.get("playerhat"))) {
//            userPlayer.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",
//                    sVars.get("playerhat"))));
//        }
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
        xCon.ex("clearthingmap THING_PLAYER");
        xCon.ex("exec " + mapPath);
        nServer.instance().addExcludingNetCmd("server",
                "cl_clearthingmap THING_PLAYER;cl_load;cv_maploaded 0");
        for(String id : nServer.instance().clientIds) {
            nServer.instance().sendMap(id);
            if(!sSettings.show_mapmaker_ui)
                xCon.ex("respawnnetplayer " + id);
        }
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipMap = new HashMap<>();
        nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
        nServer.instance().clientArgsMap.get("server").remove("virusids");
        cVars.putLong("starttime", System.currentTimeMillis());
        cVars.put("gameover", "0");
        if (cVars.getInt("gamemode") == cGameLogic.VIRUS)
            cGameLogic.resetVirusPlayers();
    }

    public static void updateEntityPositions() {
        for(String id : getPlayerIds()) {
            gPlayer obj = getPlayerById(id);
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "accelrate"};
            //check null fields
            if(!obj.containsFields(requiredFields))
                break;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < System.currentTimeMillis())
                obj.putLong("acceltick", System.currentTimeMillis()+obj.getInt("accelrate"));
            if(dx != obj.getInt("coordx") && obj.wontClipOnMove(0,dx, scene))
                obj.putInt("coordx", dx);
            if(dy != obj.getInt("coordy") && obj.wontClipOnMove(1,dy, scene))
                obj.putInt("coordy", dy);
        }

        HashMap bulletsMap = scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet obj = (gBullet) bulletsMap.get(id);
            obj.putInt("coordx", obj.getInt("coordx")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }
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
        scene.getThingMap("THING_BULLET").remove(bullet.get("id"));
        //handle damage serverside
        xCon.ex("damageplayer " + dmgvictim.get("id") + " " + adjusteddmg + " " + killerid);
        nServer.instance().addExcludingNetCmd("server",
//                "cl_spawnpopup " + dmgvictim.get("id") + " " + getRandomClientDmgString());
                "cl_spawnpopup " + dmgvictim.get("id") + " -" + adjusteddmg);
    }

    public static Collection<String> getPlayerIds() {
        return scene.getThingMap("THING_PLAYER").keySet();
    }

    public static gPlayer getPlayerById(String id) {
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }
}
