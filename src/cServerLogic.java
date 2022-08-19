import java.util.*;

public class cServerLogic {
    static int maxhp = 500;
    static int timelimit = 180000;
    static long starttime = 0;
    static int intermissionDelay = 10000;
    static boolean gameover = false;
    static int rechargehp = 1;
    static int respawnwaittime = 3000;
    static int velocityplayerbase = 8;
    static int voteskiplimit = 2;
    static long timeleft = 180000;
    static int listenPort = 5555;
    static gScene scene = new gScene();
    static gTimeEventSet timedEvents = new gTimeEventSet();

    public static void gameLoop(long loopTimeMillis) {
        cServerVars.instance().put("gametimemillis", Long.toString(loopTimeMillis));
        timedEvents.executeCommands();
        checkHealthStatus(loopTimeMillis);
        checkGameState(loopTimeMillis);
        updateEntityPositions(loopTimeMillis);
        checkBulletSplashes(loopTimeMillis);
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
            if(pvars == null || !pvars.containsKey("vels"))
                continue;
            for (int i = 0; i < 4; i++) {
                    obj.putInt("vel"+i, Integer.parseInt(pvars.get("vels").split("-")[i]));
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
                if(!checkType.contains("ITEM_"))
                    continue;
                HashMap<String, gThing> thingMap = scene.getThingMap(checkType);
                Collection<String> idCol = thingMap.keySet();
                int isize = idCol.size();
                String[] ids = idCol.toArray(new String[isize]);
                for (String itemId : ids) {
                    gItem item = (gItem) thingMap.get(itemId);
                    item.put("occupied", "0");
                    if (player.willCollideWithThingAtCoords(item,
                            player.getInt("coordx"), player.getInt("coordy"))) {
                        item.activateItem(player);
                        if(checkType.contains("ITEM_TELEPORTER"))
                            clearTeleporterFlag = 0;
                    }
                }
            }
            //after checking all items
            if(clearTeleporterFlag > 0)
                player.put("inteleporter", "0");
        }
    }

    public static void resetVirusPlayers() {
        xCon.ex("exec scripts/resetvirus");
    }

    public static void checkHealthStatus(long gameTimeMillis) {
        //recharge players health
        HashMap playersMap = scene.getThingMap("THING_PLAYER");
        for(Object id : playersMap.keySet()) {
            xCon.ex(String.format("exec scripts/rechargehealth %s", id));
        }
    }

    static void changeMap(String mapPath) {
        xCon.ex(String.format("exec scripts/changemap %s", mapPath));
        nServer.instance().sendMapToClients();
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipMap = new HashMap<>();
        nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
        nServer.instance().clientArgsMap.get("server").remove("virusids");
        timedEvents.clear();
        starttime = gTime.gameTime;
        gameover = false;
        if(cGameLogic.isGame(cGameLogic.VIRUS))
            resetVirusPlayers();
        timedEvents.put(Long.toString(starttime + timelimit), new gTimeEvent() {
            //game over
            public void doCommand() {
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
                nServer.instance().addExcludingNetCmd("server",
                        "echo changing map...");
            }
        });
        timedEvents.put(Long.toString(starttime + timelimit + intermissionDelay), new gTimeEvent() {
            //change map after game over
            public void doCommand() {
                timeleft = timelimit;
                xCon.ex("changemaprandom");
            }
        });
        for(long t = starttime+1000; t <= starttime+timelimit; t+=1000) {
            timedEvents.put(Long.toString(t), new gTimeEvent() {
                public void doCommand() {
                    if(timelimit > 0)
                        timeleft = Math.max(0, timelimit - (int) (gTime.gameTime - starttime));
                }
            });
        }
        if(cGameLogic.isGame(cGameLogic.FLAG_MASTER)) {
            for(long t = starttime+1000; t <= starttime+timelimit; t+=1000) {
                timedEvents.put(Long.toString(t), new gTimeEvent() {
                    public void doCommand() {
                        xCon.ex("exec scripts/flagmaster");
                    }
                });
            }
        }
        else if(cGameLogic.isGame(cGameLogic.GOLD_MASTER)) {
            for(long t = starttime+6000; t <= starttime+timelimit; t+=6000) {
                timedEvents.put(Long.toString(t), new gTimeEvent() {
                    public void doCommand() {
                        xCon.ex("spawnpointgiver");
                    }
                });
            }
        }
        else if(cGameLogic.isGame(cGameLogic.VIRUS)) {
            for(long t = starttime+1000; t <= starttime+timelimit; t+=1000) {
                timedEvents.put(Long.toString(t), new gTimeEvent() {
                    public void doCommand() {
                        String[] pids = getPlayerIdArray();
                        HashMap<String, String> svars = nServer.instance().clientArgsMap.get("server");
                        if(svars == null)
                            return;
                        if(!svars.containsKey("virusids")) {
                            resetVirusPlayers();
                            return;
                        }
                        boolean survivors = false;
                        for(String id : pids) {
                            if(!svars.get("virusids").contains(id)) {
                                survivors = true;
                                xCon.ex("givepoint " + id);
                            }
                        }
                        if(!survivors)
                            resetVirusPlayers();
                    }
                });
            }
        }
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
            for(String blockId : scene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = scene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(b.doesCollideWithThing(bl)) {
                    bulletsToRemoveIds.add(b.get("id"));
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
            for(String playerId : getPlayerIds()) {
                gPlayer t = getPlayerById(playerId);
                if(t != null && t.containsFields(new String[]{"coordx", "coordy"})
                        && b.doesCollideWithThing(t) && !b.get("srcid").equals(playerId)) {
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
}
