import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class eGameLogicSimulation extends eGameLogicAdapter {
    private final Queue<String> cmdQueue; //local cmd queue for server
    final gScheduler scheduledEvents;

    public eGameLogicSimulation() {
        cmdQueue = new LinkedList<>();
        scheduledEvents = new gScheduler();
    }

    public void addLocalCmd(String cmd) {
        cmdQueue.add(cmd);
    }

    public void checkLocalCmds() {
        if(cmdQueue.peek() != null)
            xMain.shellLogic.console.ex(cmdQueue.remove());
    }

    public void update() {
        super.update();
        if(!sSettings.IS_SERVER)
            return;
        long gameTimeMillis = sSettings.gameTime;
        xMain.shellLogic.serverVars.put("gametimemillis", Long.toString(gameTimeMillis));
        checkLocalCmds();
        scheduledEvents.executeCommands();
        xMain.shellLogic.console.ex("exec scripts/sv_checkgamestate");
        checkGameItems();
        updateEntityPositions(gameTimeMillis);
        sSettings.tickReportSimulation = getTickReport();
    }

    private void checkGameItems() {
        ConcurrentHashMap<String, gThing> playerMap = xMain.shellLogic.serverScene.getThingMap("THING_PLAYER");
        ConcurrentHashMap<String, gThing> itemsMap = xMain.shellLogic.serverScene.getThingMap("THING_ITEM");
        Queue<gThing> playerQueue = new LinkedList<>();
        Queue<gThing> itemsQueue = new LinkedList<>();
        //TODO: fix concurrent modification by capturing a copy of the keyset and iterating over that instead
        ArrayList<String> keysetcopy = new ArrayList<>(itemsMap.keySet());
        for(String id : keysetcopy) {
            itemsQueue.add(itemsMap.get(id));
        }
        while(itemsQueue.size() > 0) {
            gItem item = (gItem) itemsQueue.remove();
            item.occupied = 0;
            for (String id : playerMap.keySet()) {
                playerQueue.add(playerMap.get(id));
            }
            while(playerQueue.size() > 0) {
                gPlayer player = (gPlayer) playerQueue.remove();
                if(player.collidesWithThing(item))
                    item.activateItem(player);
            }
        }
    }


    private void updateEntityPositions(long gameTimeMillis) {
        nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
        for(String id : svMap.keys()) {
            gPlayer obj = xMain.shellLogic.serverScene.getPlayerById(id);
            if(obj == null)
                continue;
            int dx = obj.coords[0] + obj.vel3 - obj.vel2;
            int dy = obj.coords[1] + obj.vel1 - obj.vel0;

            if (obj.acceltick < gameTimeMillis) {
                obj.acceltick = gameTimeMillis + obj.acceldelay;
                //user player
                if(obj.mov0 > 0)
                    obj.vel0 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel0 + obj.accelrate);
                else
                    obj.vel0 = Math.max(0, obj.vel0 - obj.decelrate);
                if(obj.mov1 > 0)
                    obj.vel1 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel1 + obj.accelrate);
                else
                    obj.vel1 = Math.max(0, obj.vel1 - obj.decelrate);
                if(obj.mov2 > 0)
                    obj.vel2 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel2 + obj.accelrate);
                else
                    obj.vel2 = Math.max(0, obj.vel2 - obj.decelrate);
                if(obj.mov3 > 0)
                    obj.vel3 = Math.min(sSettings.clientVelocityPlayerBase, obj.vel3 + obj.accelrate);
                else
                    obj.vel3 = Math.max(0, obj.vel3 - obj.decelrate);
            }

            if(obj.wontClipOnMove(dx, obj.coords[1], xMain.shellLogic.serverScene))
                obj.coords[0] = dx;
            else {
                if(obj.vel2 > obj.vel3) {
//                    obj.put("vel3", obj.get("vel2")); //bounce
                    obj.vel2 = 0;
                }
                else {
//                    obj.put("vel2", obj.get("vel3")); //bounce
                    obj.vel3 = 0;
                }
            }
            if(obj.wontClipOnMove(obj.coords[0], dy, xMain.shellLogic.serverScene))
                obj.coords[1] = dy;
            else {
                if(obj.vel0 > obj.vel1) {
//                    obj.put("vel1", obj.get("vel0")); //bounce
                    obj.vel0 = 0;
                }
                else {
//                    obj.put("vel0", obj.get("vel1")); //bounce
                    obj.vel1 = 0;
                }
            }
        }

        //bullets
        try {
            ConcurrentHashMap<String, gThing> thingMap = xMain.shellLogic.serverScene.getThingMap("THING_BULLET");
            Queue<gThing> checkQueue = new LinkedList<>();
            String[] keys = thingMap.keySet().toArray(new String[0]);
            for (String id : keys) {
                checkQueue.add(thingMap.get(id));
            }
            while(checkQueue.size() > 0) {
                gThing obj = checkQueue.remove();
                obj.coords[0] -= (int) (gWeapons.fromCode(obj.src).bulletVel*Math.cos(obj.fv+Math.PI/2));
                obj.coords[1] -= (int) (gWeapons.fromCode(obj.src).bulletVel*Math.sin(obj.fv+Math.PI/2));
            }
            checkBulletSplashes(gameTimeMillis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkBulletSplashes(long gameTimeMillis) {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gThing> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gThing> pseeds = new ArrayList<>();
        ConcurrentHashMap<String, gThing> bulletsMap = xMain.shellLogic.serverScene.getThingMap("THING_BULLET");
        nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
        Queue<gThing> checkQueue = new LinkedList<>();
        String[] keys = bulletsMap.keySet().toArray(new String[0]);
        for (String id : keys) {
            checkQueue.add(bulletsMap.get(id));
        }
        while(checkQueue.size() > 0) {
            gThing b = checkQueue.remove();
            if(gameTimeMillis - b.timestamp > b.ttl) {
                bulletsToRemoveIds.add(b.id);
                //grenade explosion
                if(b.src == gWeapons.launcher)
                    pseeds.add(b);
                continue;
            }
            for(String blockId : xMain.shellLogic.serverScene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = xMain.shellLogic.serverScene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(b.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(b.id);
                    if(b.src == gWeapons.launcher)
                        pseeds.add(b);
                }
            }
            for(String playerId : svMap.keys()) {
                gPlayer t = xMain.shellLogic.serverScene.getPlayerById(playerId);
                if(t != null && b.collidesWithThing(t) && !b.srcId.equals(playerId)) {
                    bulletsToRemovePlayerMap.put(t, b);
                    if(b.src == gWeapons.launcher)
                        pseeds.add(b);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gThing pseed : pseeds)
                gWeapons.createGrenadeExplosion(pseed);
        }
        for(Object bulletId : bulletsToRemoveIds) {
            xMain.shellLogic.serverScene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            gThing b = bulletsToRemovePlayerMap.get(p);
            //calculate dmg
            int dmg = b.dmg - (int)((double)b.dmg/2 *((Math.abs(Math.max(0, sSettings.gameTime - b.timestamp))/(double)b.ttl))); // dmg falloff based on age of bullet
            xMain.shellLogic.serverScene.getThingMap("THING_BULLET").remove(b.id);
            //handle damage serverside
            xMain.shellLogic.console.ex(String.format("damageplayer %s %d %s", p.id, dmg, b.srcId));
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        sSettings.tickReportSimulation = 0;
    }
}
