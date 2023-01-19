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
        timedEvents.executeCommands();
        xCon.ex("exec scripts/checkgamestate");
        checkGameState();
        updateEntityPositions(loopTimeMillis);
        checkBulletSplashes(loopTimeMillis);
    }

    private static void checkGameState() {
        for(String id : nServer.instance().masterStateMap.keys()) {
            if(id.equals(uiInterface.uuid)) //ignore this part if we are server player (figure out why)
                continue;
            gPlayer obj = getPlayerById(id);
            if(obj == null)
                continue;
            nState objState = nServer.instance().masterStateMap.get(id);
            obj.put("vel0", objState.get("vel0"));
            obj.put("vel1", objState.get("vel1"));
            obj.put("vel2", objState.get("vel2"));
            obj.put("vel3", objState.get("vel3"));
        }
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
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }
}
