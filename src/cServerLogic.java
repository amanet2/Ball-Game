import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class cServerLogic {
    static int maxhp = 500;
    static int timelimit = 180000;
    static long flagmastertime = 0;
    static int delayhp = 3600;
    static long starttime = 0;
    static long intermissiontime = -1;
    static int intermissionDelay = 10000;
    static boolean gameover = false;
    static int rechargehp = 1;
    static long virustime = 0;
    static long goldspawntime = 0;
    static int respawnwaittime = 3000;
    static int velocityplayerbase = 8;
    static int voteskiplimit = 2;
    static long timeleft = 180000;
    static int listenPort = 5555;
    static gScene scene = new gScene();

    public static void gameLoop(long loopTimeMillis) {
        checkTimeRemaining(loopTimeMillis);
        checkHealthStatus(loopTimeMillis);
        checkForMapChange(loopTimeMillis);
        checkGameState(loopTimeMillis);
        updateEntityPositions(loopTimeMillis);
        checkBulletSplashes(loopTimeMillis);
    }

    public static void checkTimeRemaining(long gameTimeMillis) {
        if(timelimit > 0)
            timeleft = Math.max(0, timelimit - (int) (gameTimeMillis - starttime));
        else
            timeleft = -1;
    }

    public static void checkGameState(long gameTimeMillis) {
        String[] pids = getPlayerIdArray();
        for(String id : pids) {
            //this is needed when server user joins his own games
            if(id.equals(uiInterface.uuid))
                continue;
            gPlayer obj = getPlayerById(id);
            if(obj == null)
                continue;
            HashMap<String, String> pvars = nServer.instance().clientArgsMap.get(obj.get("id"));
            if(pvars == null)
                continue;
            for (int i = 0; i < 4; i++) {
                if(pvars.containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(pvars.get("vels").split("-")[i]));
            }
        }
        HashMap<String, String> svars = nServer.instance().clientArgsMap.get("server");
        if(svars != null) {
            if(svars.containsKey("flagmasterid")) {
                if(flagmastertime < gameTimeMillis) {
                    xCon.ex("givepoint " + svars.get("flagmasterid"));
                    flagmastertime = gameTimeMillis + 1000;
                }
            }
            if(cGameLogic.isGame(cGameLogic.GOLD_MASTER) && !sSettings.show_mapmaker_ui) {
                if(goldspawntime < gameTimeMillis) {
                    xCon.ex("spawnpointgiver");
                    goldspawntime = gameTimeMillis + 6000;
                }
            }
            if(svars.containsKey("virusids")) {
                if(virustime < gameTimeMillis) {
                    boolean survivors = false;
                    for(String id : getPlayerIdArray()) {
                        if(!svars.get("virusids").contains(id)) {
                            survivors = true;
                            xCon.ex("givepoint " + id);
                        }
                    }
                    virustime = gameTimeMillis + 1000;
                    if(!survivors) {
                        cGameLogic.resetVirusPlayers();
                    }
                }
            }
            else if(cGameLogic.isGame(cGameLogic.VIRUS)) {
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
                if(checkType.contains("ITEM_")) {
                    HashMap<String, gThing> thingMap = scene.getThingMap(checkType);
                    Collection<String> idCol = thingMap.keySet();
                    int isize = idCol.size();
                    String[] ids = idCol.toArray(new String[isize]);
                    for (String itemId : ids) {
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
        if(!sSettings.show_mapmaker_ui && !gameover) {
            //conditions
            if((timeleft == 0 && intermissiontime < 0)) {
                gameover = true;
            }
            if(gameover) {
                String highestId = gScoreboard.getWinnerId();
                if(highestId.length() > 0) {
                    gScoreboard.incrementScoreFieldById(highestId, "wins");
                    nServer.instance().addExcludingNetCmd("server", "echo "
                            + nServer.instance().clientArgsMap.get(highestId).get("name")
                            + "#" + nServer.instance().clientArgsMap.get(highestId).get("color")
                            + " wins#" + nServer.instance().clientArgsMap.get(highestId).get("color"));
                }
                int toplay = (int) (Math.random() * eManager.winSoundFileSelection.length);
                nServer.instance().addExcludingNetCmd("server",
                        "playsound sounds/win/"+eManager.winSoundFileSelection[toplay]);
                intermissiontime = gameTimeMillis + intermissionDelay;
                nServer.instance().addExcludingNetCmd("server",
                        "echo changing map...");
            }
        }
    }

    public static void checkHealthStatus(long gameTimeMillis) {
        //respawn ready players
        HashMap<String, HashMap<String, String>> argsMap = nServer.instance().clientArgsMap;
        Long currentTime = gameTimeMillis;
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
            int pHp = (int) p.getDouble("stockhp");
            if(rechargehp > 0 && pHp < maxhp && p.getLong("hprechargetime") + delayhp < gameTimeMillis)
                p.putInt("stockhp", Math.min(pHp + rechargehp, maxhp));
            else if(pHp > maxhp)
                p.putInt("stockhp", maxhp);
        }
    }

    public static void changeBotWeapon(gPlayer cl, int newweapon, boolean fromPowerup) {
        HashMap botsMap = scene.getThingMap("THING_BOTPLAYER");
        if(botsMap.size() > 0 && !(!fromPowerup && newweapon != 0)) {
            nServer.instance().clientArgsMap.get(cl.get("id")).put("weapon", Integer.toString(newweapon));
            cl.checkSpriteFlip();
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

    public static void checkForMapChange(long gameTimeMillis) {
        if(intermissiontime > 0 && intermissiontime < gameTimeMillis) {
            intermissiontime = -1;
            timeleft = timelimit;
            xCon.ex("changemaprandom");
        }
    }

    static void changeMap(String mapPath, long gameTimeMillis) {
        xCon.ex("clearthingmap THING_PLAYER");
        xCon.ex("exec " + mapPath);
        nServer.instance().addExcludingNetCmd("server",
                "cl_clearthingmap THING_PLAYER;cl_load;cv_maploaded 0");
        nServer.instance().sendMapToClients();
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipMap = new HashMap<>();
        nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
        nServer.instance().clientArgsMap.get("server").remove("virusids");
        starttime = gameTimeMillis;
        gameover = false;
        if(cGameLogic.isGame(cGameLogic.VIRUS))
            cGameLogic.resetVirusPlayers();
    }

    public static void updateEntityPositions(long gameTimeMillis) {
        for(String id : getPlayerIds()) {
            gPlayer obj = getPlayerById(id);
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "accelrate"};
            //check null fields
            if(!obj.containsFields(requiredFields))
                break;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < gameTimeMillis)
                obj.putLong("acceltick", gameTimeMillis + obj.getInt("accelrate"));
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

    public static void checkBulletSplashes(long gameTimeMillis) {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap<String, gThing> bulletsMap = scene.getThingMap("THING_BULLET");
        for(String id : bulletsMap.keySet()) {
            gBullet b = (gBullet) bulletsMap.get(id);
            if(gameTimeMillis - b.getLong("timestamp") > b.getInt("ttl")){
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
            createDamagePopup(p, bulletsToRemovePlayerMap.get(p), gameTimeMillis);
        }
    }

    //call this everytime a bullet intersects a player
    public static void createDamagePopup(gPlayer dmgvictim, gBullet bullet, long gameTimeMillis) {
        //get shooter details
        String killerid = bullet.get("srcid");
        //calculate dmg
        int adjusteddmg = bullet.getInt("dmg") - (int)((double)bullet.getInt("dmg")/2
                *((Math.abs(Math.max(0, gameTimeMillis - bullet.getLong("timestamp"))
        )/(double)bullet.getInt("ttl"))));
        scene.getThingMap("THING_BULLET").remove(bullet.get("id"));
        //handle damage serverside
        xCon.ex("damageplayer " + dmgvictim.get("id") + " " + adjusteddmg + " " + killerid);
        nServer.instance().addExcludingNetCmd("server",
                "cl_spawnpopup " + dmgvictim.get("id") + " -" + adjusteddmg);
    }

    public static Collection<String> getPlayerIds() {
        return scene.getThingMap("THING_PLAYER").keySet();
    }

    public static String[] getPlayerIdArray() {
        Collection<String> pColl = scene.getThingMap("THING_PLAYER").keySet();
        int psize = pColl.size();
        return pColl.toArray(new String[psize]);
    }

    public static gPlayer getPlayerById(String id) {
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }

    public static boolean isOccupied(gItem spawnpoint) {
        for(String id : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
            gPlayer player = (gPlayer) cServerLogic.scene.getThingMap("THING_PLAYER").get(id);
            Shape bounds = new Rectangle(spawnpoint.getInt("coordx"), spawnpoint.getInt("coordy"),
                    spawnpoint.getInt("dimw"), spawnpoint.getInt("dimh"));
            if(bounds.intersects(new Rectangle(
                    player.getInt("coordx"), player.getInt("coordy"), player.getInt("dimw"),
                    player.getInt("dimh"))))
                return true;
        }
        return false;
    }
}
