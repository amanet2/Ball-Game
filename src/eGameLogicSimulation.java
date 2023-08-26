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
        ConcurrentHashMap<String, gThing> balldepositmap = xMain.shellLogic.serverScene.getThingMap("ITEM_BALLDEPOSIT");
        ConcurrentHashMap<String, gThing> ballmap = xMain.shellLogic.serverScene.getThingMap("ITEM_BALL");
        //TODO: fix concurrent modification by capturing a copy of the keyset and iterating over that instead
        ArrayList<String> itemKeySetCopy = new ArrayList<>(itemsMap.keySet());
        ArrayList<String> balldepositkeysetcopy = new ArrayList<>(balldepositmap.keySet());
        ArrayList<String> ballkeysetcopy = new ArrayList<>(ballmap.keySet());
        ArrayList<String> playerKeySetCopy = new ArrayList<>(playerMap.keySet());
        for(String iid : itemKeySetCopy) {
            gItem item = (gItem) itemsMap.get(iid);
            item.occupied = 0;
            updateThingPosition(item, sSettings.gameTime);
            for(String pid : playerKeySetCopy) {
                if(!playerMap.containsKey(pid))
                    continue;
                gPlayer player = (gPlayer) playerMap.get(pid);
                if(!item.type.equals("ITEM_BALLDEPOSIT") && player.collidesWithThing(item))
                    item.activateItem(player);
            }
        }
        for(String bdid : balldepositkeysetcopy) {
            gItem bd = (gItem) balldepositmap.get(bdid);
            for(String id : ballkeysetcopy) {
                gItem b = (gItem) itemsMap.get(id);
                if(bd.collidesWithThing(b)) {
                    bd.activateItem(b);
                }
            }
        }
    }

    private void updateThingPosition(gThing obj, long gameTimeMillis) {
            if(obj == null || !obj.type.equals("ITEM_BALL"))
                return;
            int dx = obj.coords[0] + obj.vel3 - obj.vel2;
            int dy = obj.coords[1] + obj.vel1 - obj.vel0;

            // TRACKS PLAYER
//            gPlayer player = xMain.shellLogic.getUserPlayer();
//            if(player != null) {
//                if(player.coords[1] > obj.coords[1]) {
//                    obj.mov0 = 0;
//                    obj.mov1 = 1;
//                }
//                else if(player.coords[1] < obj.coords[1]){
//                    obj.mov0 = 1;
//                    obj.mov1 = 0;
//                }
//                else {
//                    obj.mov0 = 0;
//                    obj.mov1 = 0;
//                }
//                if(player.coords[0] > obj.coords[0]) {
//                    obj.mov2 = 0;
//                    obj.mov3 = 1;
//                }
//                else if(player.coords[0] < obj.coords[0]){
//                    obj.mov2 = 1;
//                    obj.mov3 = 0;
//                }
//                else {
//                    obj.mov2 = 0;
//                    obj.mov3 = 0;
//                }
//            }

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

        if(obj.coords[0] != dx || obj.coords[1] != dy) { //want to NOT add a server command every tick here
            if(obj.botWontClipOnMove(dx, obj.coords[1], xMain.shellLogic.serverScene))
                obj.coords[0] = dx;
            if(obj.botWontClipOnMove(obj.coords[0], dy, xMain.shellLogic.serverScene))
                obj.coords[1] = dy;
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

            //TODO: come up with a way to get "normal vector" from surface or player being collided with
            // add a "collidedPlayer" arg to gThing and get velocity
            //TODO UPDATE: Looks good, just need at-rest players to get launched by players colliding into them
            //TODO UPDATE: looks better, but bounces are restricted to 4 basic dirs
            if(obj.wontClipOnMove(dx, obj.coords[1], xMain.shellLogic.serverScene))
                obj.coords[0] = dx;
            else {
                if(obj.vel2 > obj.vel3) {
                    int collidedPlayerVel = obj.collidedPlayer == null ? 0 : obj.collidedPlayer.vel3;
                    if(obj.collidedPlayer != null && obj.collidedPlayer.mov0 == 0 && obj.collidedPlayer.mov1 == 0 && obj.collidedPlayer.mov2 == 0 && obj.collidedPlayer.mov3 == 0)
                        obj.collidedPlayer.vel2 = Math.max(0, obj.vel2 - 1);
                    obj.vel3 = Math.max(0, collidedPlayerVel + obj.vel2/2 - obj.vel0/2 - obj.vel1/2); //bounce
                    obj.vel2 = 0;
                }
                else {
                    int collidedPlayerVel = obj.collidedPlayer == null ? 0 : obj.collidedPlayer.vel2;
                    if(obj.collidedPlayer != null && obj.collidedPlayer.mov0 == 0 && obj.collidedPlayer.mov1 == 0 && obj.collidedPlayer.mov2 == 0 && obj.collidedPlayer.mov3 == 0)
                        obj.collidedPlayer.vel3 = Math.max(0, obj.vel3 - 1);
                    obj.vel2 = Math.max(0, collidedPlayerVel + obj.vel3/2 - obj.vel0/2 - obj.vel1/2); //bounce
                    obj.vel3 = 0;
                }
            }
            if(obj.wontClipOnMove(obj.coords[0], dy, xMain.shellLogic.serverScene))
                obj.coords[1] = dy;
            else {
                if(obj.vel0 > obj.vel1) {
                    int collidedPlayerVel = obj.collidedPlayer == null ? 0 : obj.collidedPlayer.vel1;
                    if(obj.collidedPlayer != null && obj.collidedPlayer.mov0 == 0 && obj.collidedPlayer.mov1 == 0 && obj.collidedPlayer.mov2 == 0 && obj.collidedPlayer.mov3 == 0)
                        obj.collidedPlayer.vel0 = Math.max(0, obj.vel0 - 1);
                    obj.vel1 = Math.max(0, collidedPlayerVel + obj.vel0/2 - obj.vel2/2 - obj.vel3/2); //bounce
                    obj.vel0 = 0;
                }
                else {
                    int collidedPlayerVel = obj.collidedPlayer == null ? 0 : obj.collidedPlayer.vel0;
                    if(obj.collidedPlayer != null && obj.collidedPlayer.mov0 == 0 && obj.collidedPlayer.mov1 == 0 && obj.collidedPlayer.mov2 == 0 && obj.collidedPlayer.mov3 == 0)
                        obj.collidedPlayer.vel1 = Math.max(0, obj.vel1 - 1);
                    obj.vel0 = Math.max(0, collidedPlayerVel + obj.vel1/2 - obj.vel2/2 - obj.vel3/2); //bounce
                    obj.vel1 = 0;
                }
            }
            obj.collidedPlayer = null;
            if(sSettings.botsPaused < 1 && obj.id.startsWith("bot") && obj.botThinkTime < sSettings.gameTime) {
                obj.botThinkTime = sSettings.gameTime + sSettings.botThinkTimeDelay;
                gPlayer player = getClosestPlayer(obj);
                if(player != null) {
                    if(player.coords[1] > obj.coords[1]) {
                        obj.mov0 = 0;
                        obj.mov1 = 1;
                    }
                    else if(player.coords[1] < obj.coords[1]){
                        obj.mov0 = 1;
                        obj.mov1 = 0;
                    }
                    else {
                        obj.mov0 = 0;
                        obj.mov1 = 0;
                    }
                    if(player.coords[0] > obj.coords[0]) {
                        obj.mov2 = 0;
                        obj.mov3 = 1;
                    }
                    else if(player.coords[0] < obj.coords[0]){
                        obj.mov2 = 1;
                        obj.mov3 = 0;
                    }
                    else {
                        obj.mov2 = 0;
                        obj.mov3 = 0;
                    }
                    //point at player
                    double bdx = player.coords[0] + player.dims[0]/2 - obj.coords[0] + obj.dims[0]/2;
                    double bdy = player.coords[1] + player.dims[1]/2 - obj.coords[1] + obj.dims[1]/2;
                    double angle = Math.atan2(bdy, bdx);
                    if (angle < 0)
                        angle += 2*Math.PI;
                    angle += Math.PI/2;
                    double randomOffset = Math.random()*3;
                    if(randomOffset > 2)
                        angle -= Math.PI/4;
                    else if(randomOffset > 1)
                        angle += Math.PI/4;
                    obj.fv = angle;
                }
            }
        }
        // bots

        //bullets
        try {
            ConcurrentHashMap<String, gThing> thingMap = xMain.shellLogic.serverScene.getThingMap("THING_BULLET");
            for (String id : thingMap.keySet()) {
                gThing obj = thingMap.get(id);
                obj.coords[0] -= (int) (gWeapons.fromCode(obj.src).bulletVel*Math.cos(obj.fv+Math.PI/2));
                obj.coords[1] -= (int) (gWeapons.fromCode(obj.src).bulletVel*Math.sin(obj.fv+Math.PI/2));
            }
            checkBulletSplashes();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public gPlayer getClosestPlayer(gPlayer src) {
        gPlayer closest = null;
        int closestDist = 1000000;
        for(String id : xMain.shellLogic.serverScene.getThingMap("THING_PLAYER").keySet()) {
            if(id.equals(src.id))
                continue;
            gPlayer dst = (gPlayer) xMain.shellLogic.serverScene.getThingMap("THING_PLAYER").get(id);
            int x1 = src.coords[0];
            int y1 = src.coords[1];
            int x2 = dst.coords[0];
            int y2 = dst.coords[1];
            int dist = (int) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
            if(dist < closestDist) {
                closest = dst;
                closestDist = dist;
            }
        }
        if(closestDist < sSettings.botShootRange && src.botShootTime < sSettings.gameTime) {
            src.botShootTime = sSettings.gameTime + sSettings.botShootTimeDelay;
            xMain.shellLogic.serverNetThread.addNetCmd("server", String.format("fireweapon %s %d", src.id, src.weapon));
            xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", String.format("cl_fireweapon %s %d", src.id, src.weapon));
//            xMain.shellLogic.console.ex(String.format("fireweapon %s %d", src.id, src.weapon));
        }
        return closest;
    }

    private void checkBulletSplashes() {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gThing> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gThing> pseeds = new ArrayList<>();
        ConcurrentHashMap<String, gThing> bulletsMap = xMain.shellLogic.serverScene.getThingMap("THING_BULLET");
        for(String id : bulletsMap.keySet()) {
            gThing b = bulletsMap.get(id);
            for(String blockId : xMain.shellLogic.serverScene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = xMain.shellLogic.serverScene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(b.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(b.id);
                    if(b.src == gWeapons.launcher)
                        pseeds.add(b);
                }
            }
            for(String playerId : xMain.shellLogic.serverScene.getThingMapIds("THING_PLAYER")) {
                gPlayer t = xMain.shellLogic.serverScene.getPlayerById(playerId);
                if(t != null && b.collidesWithThing(t) && !b.srcId.equals(playerId)) {
                    bulletsToRemovePlayerMap.put(t, b);
                    if(b.src == gWeapons.launcher)
                        pseeds.add(b);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gThing pseed : pseeds) {
                gWeapons.createGrenadeExplosion(pseed, xMain.shellLogic.serverScene);
            }
        }
        for(String bulletId : bulletsToRemoveIds) {
            xMain.shellLogic.serverScene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            gThing b = bulletsToRemovePlayerMap.get(p);
            //calculate dmg
//            int dmg = b.dmg - (int)((double)b.dmg/2 *((Math.abs(Math.max(0, sSettings.gameTime - b.timestamp))/(double)b.ttl))); // dmg falloff based on age of bullet
            xMain.shellLogic.serverScene.getThingMap("THING_BULLET").remove(b.id);
            //handle damage serverside
            xMain.shellLogic.console.ex(String.format("damageplayer %s %d %s", p.id, b.dmg, b.srcId));
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        sSettings.tickReportSimulation = 0;
    }
}
