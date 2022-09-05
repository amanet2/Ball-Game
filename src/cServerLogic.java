import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class cServerLogic {
    static int maxhp = 500;
    static int timelimit = 180000;
    static int intermissionDelay = 10000;
    static int rechargehp = 1;
    static int respawnwaittime = 3000;
    static int velocityplayerbase = 8;
    static int voteskiplimit = 2;
    static long timeleft = 120000;
    static int listenPort = 5555;
    static gScene scene = new gScene();
    static gTimeEventSet timedEvents = new gTimeEventSet();

    public static void gameLoop(long loopTimeMillis) {
        cServerVars.instance().put("gametimemillis", Long.toString(loopTimeMillis));
        timedEvents.executeCommands();
        checkHealthStatus();
        checkGameState();
        updateEntityPositions(loopTimeMillis);
        checkBulletSplashes(loopTimeMillis);
    }

    public static void checkGameState() {
        String[] pids = getPlayerIdArray();
        for(String id : pids) {
            if(id.equals(uiInterface.uuid)) //ignore this part if we are server player
                continue;
            gPlayer obj = getPlayerById(id);
            if(obj == null)
                continue;
            nState objState = nServer.instance().masterStateMap.get(obj.get("id"));
            obj.putInt("vel0", Integer.parseInt(objState.get("vels").split("-")[0]));
            obj.putInt("vel1", Integer.parseInt(objState.get("vels").split("-")[1]));
            obj.putInt("vel2", Integer.parseInt(objState.get("vels").split("-")[2]));
            obj.putInt("vel3", Integer.parseInt(objState.get("vels").split("-")[3]));
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

    public static void checkHealthStatus() {
        //recharge players health
        for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
            xCon.ex(String.format("exec scripts/rechargehealth %s", id));
        }
    }

    static void changeMap(String mapPath) {
        xCon.ex(String.format("exec scripts/changemap %s", mapPath));
        nServer.instance().sendMapToClients();
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipList = new ArrayList<>();
        nServer.instance().serverVars.remove("flagmasterid");
        nServer.instance().serverVars.remove("virusids");
        timedEvents.clear();
        long starttime = gTime.gameTime;
        if(cGameLogic.isGame(cGameLogic.VIRUS))
            xCon.ex("exec scripts/resetvirus");
        timedEvents.put(Long.toString(starttime + timelimit), new gTimeEvent() {
            //game over
            public void doCommand() {
                String highestId = gScoreboard.getWinnerId();
                if(highestId.length() > 0) {
                    gScoreboard.incrementScoreFieldById(highestId, "wins");
                    nServer.instance().addExcludingNetCmd("server", "echo "
                            + nServer.instance().masterStateMap.get(highestId).get("name")
                            + "#" + nServer.instance().masterStateMap.get(highestId).get("color")
                            + " wins#" + nServer.instance().masterStateMap.get(highestId).get("color"));
                }
                int toplay = (int) (Math.random() * eManager.winSoundFileSelection.length);
                nServer.instance().addExcludingNetCmd("server",
                        "playsound sounds/win/"+eManager.winSoundFileSelection[toplay]);
                nServer.instance().addExcludingNetCmd("server",
                        "echo changing map...");
            }
        });
        if(sSettings.show_mapmaker_ui)
            return;
        timedEvents.put(Long.toString(starttime + timelimit + intermissionDelay), new gTimeEvent() {
            //change map after game over
            public void doCommand() {
                xCon.ex("changemaprandom");
            }
        });
        for(long t = starttime+1000; t <= starttime+timelimit; t+=1000) {
            long finalT = t;
            timedEvents.put(Long.toString(t), new gTimeEvent() {
                public void doCommand() {
                    if(timelimit > 0)
                        timeleft = Math.max(0, (starttime + timelimit) - finalT);
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
                        HashMap<String, String> svars = nServer.instance().serverVars;
                        if(svars == null)
                            return;
                        if(!svars.containsKey("virusids")) {
                            xCon.ex("exec scripts/resetvirus");
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
                            xCon.ex("exec scripts/resetvirus");
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
                continue;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < gameTimeMillis)
                obj.putLong("acceltick", gameTimeMillis + obj.getInt("accelrate"));
            if(dx != obj.getInt("coordx") && obj.wontClipOnMove(0,dx, scene))
                obj.putInt("coordx", dx);
            if(dy != obj.getInt("coordy") && obj.wontClipOnMove(1,dy, scene))
                obj.putInt("coordy", dy);
        }

        HashMap<String, gThing> bulletsMap = scene.getThingMap("THING_BULLET");
        for(String id : bulletsMap.keySet()) {
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
            if(gameTimeMillis - b.getLong("timestamp") > b.getInt("ttl")) {
                bulletsToRemoveIds.add(id);
                //grenade explosion
                if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                    pseeds.add(b);
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
            createDamagePopup(p, bulletsToRemovePlayerMap.get(p));
        }
    }

    public static void createDamagePopup(gPlayer victim, gBullet bullet) {
        //calculate dmg
        int dmg = bullet.getInt("dmg") - (int)((double)bullet.getInt("dmg")/2
                *((Math.abs(Math.max(0, gTime.gameTime - bullet.getLong("timestamp"))
        )/(double)bullet.getInt("ttl")))); // dmg falloff based on age of bullet
        scene.getThingMap("THING_BULLET").remove(bullet.get("id"));
        //handle damage serverside
        xCon.ex(String.format("exec scripts/sv_createpopupdmg %s %d %s", victim.get("id"), dmg, bullet.get("srcid")));
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
