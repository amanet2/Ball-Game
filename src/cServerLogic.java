import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class cServerLogic {
    static int timelimit = 180000;
    static long timeleft = 120000;
    static int listenPort = 5555;
    static gScene scene;
    static gTimeEventSet timedEvents = new gTimeEventSet();

    public static void gameLoop(long loopTimeMillis) {
        cServerVars.instance().put("gametimemillis", Long.toString(loopTimeMillis));
//        nServer.instance().processPackets();
        nServer.instance().checkForUnhandledQuitters();
        timedEvents.executeCommands();
        xCon.ex("exec scripts/checkgamestate");
        checkGameState();
        updateEntityPositions(loopTimeMillis);
        checkBulletSplashes(loopTimeMillis);
    }

    private static void checkGameState() {
        // NEW ITEMS CHECKING.  ACTUALLY WORKS
        HashMap<String, gThing> playerMap = scene.getThingMap("THING_PLAYER");
        for (String playerId : playerMap.keySet()) {
            gPlayer player = (gPlayer) playerMap.get(playerId);
            //check null fields
            if (!player.containsFields(new String[]{"coordx", "coordy"}))
                continue;
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
                    if (player.collidesWithThing(item)) {
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

    static void changeMap(String mapPath) {
        xCon.ex(String.format("exec scripts/changemap %s", mapPath));
        nServer.instance().sendMapToClients();
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipList = new ArrayList<>();
        timedEvents.clear();
        if(sSettings.show_mapmaker_ui)
            return;
        long starttime = gTime.gameTime;
        for(long t = starttime+1000; t <= starttime+timelimit; t+=1000) {
            long lastT = t;
            timedEvents.put(Long.toString(t), new gTimeEvent() {
                public void doCommand() {
                    if(timelimit > 0)
                        timeleft = Math.max(0, (starttime + timelimit) - lastT);
                }
            });
        }
        xCon.ex("exec scripts/startgame " + cClientLogic.gamemode);
    }

    private static void updateEntityPositions(long gameTimeMillis) {
        for(String id : nServer.instance().masterStateMap.keys()) {
            gPlayer obj = getPlayerById(id);
            if(obj == null)
                continue;
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "acceldelay", "accelrate",
                    "decelrate"
            };
            //check null fields
            if(!obj.containsFields(requiredFields))
                continue;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < gameTimeMillis) {
                obj.putLong("acceltick", gameTimeMillis + obj.getInt("acceldelay"));
                for (int i = 0; i < 4; i++) {
                    if (obj.getInt("mov" + i) > 0) {
                        obj.putInt("vel" + i, (Math.min(cClientLogic.velocityPlayer,
                                obj.getInt("vel" + i) + obj.getInt("accelrate"))));
                    }
                    else
                        obj.putInt("vel" + i, Math.max(0, obj.getInt("vel" + i) - obj.getInt("decelrate")));
                }
            }
            if(obj.wontClipOnMove(dx, obj.getInt("coordy"), scene))
                obj.putInt("coordx", dx);
            else {
                if(obj.getInt("vel2") > obj.getInt("vel3"))
                    obj.putInt("vel2", 0);
                else
                    obj.putInt("vel3", 0);
            }
            if(obj.wontClipOnMove(obj.getInt("coordx"), dy, scene))
                obj.putInt("coordy", dy);
            else {
                if(obj.getInt("vel0") > obj.getInt("vel1"))
                    obj.putInt("vel0", 0);
                else
                    obj.putInt("vel1", 0);
            }
            nState objState = nServer.instance().masterStateMap.get(id);
            if(objState != null) {
                objState.put("coords", obj.get("coordx") + ":" + obj.get("coordy"));
                objState.put("vel0", obj.get("vel0"));
                objState.put("vel1", obj.get("vel1"));
                objState.put("vel2", obj.get("vel2"));
                objState.put("vel3", obj.get("vel3"));
            }
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

    private static void checkBulletSplashes(long gameTimeMillis) {
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
                if(b.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(b.get("id"));
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
            for(String playerId : nServer.instance().masterStateMap.keys()) {
                gPlayer t = getPlayerById(playerId);
                if(t != null && t.containsFields(new String[]{"coordx", "coordy"})
                        && b.collidesWithThing(t) && !b.get("srcid").equals(playerId)) {
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
            gBullet b = bulletsToRemovePlayerMap.get(p);
            //calculate dmg
            int dmg = b.getInt("dmg") - (int)((double)b.getInt("dmg")/2
                    *((Math.abs(Math.max(0, gTime.gameTime - b.getLong("timestamp"))
            )/(double)b.getInt("ttl")))); // dmg falloff based on age of bullet
            scene.getThingMap("THING_BULLET").remove(b.get("id"));
            //handle damage serverside
            xCon.ex(String.format("exec scripts/sv_createpopupdmg %s %d %s", p.get("id"), dmg, b.get("srcid")));
        }
    }

    public static gPlayer getPlayerById(String id) {
        return scene.getPlayerById(id);
    }
}
