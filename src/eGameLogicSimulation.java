import java.util.*;

public class eGameLogicSimulation extends eGameLogicAdapter {
    private final Queue<String> cmdQueue; //local cmd queue for server
    final gTimeEventSet scheduledEvents;

    public eGameLogicSimulation() {
        cmdQueue = new LinkedList<>();
        scheduledEvents = new gTimeEventSet();
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
        long gameTimeMillis = gTime.gameTime;
        xMain.shellLogic.serverVars.put("gametimemillis", Long.toString(gameTimeMillis));
        checkLocalCmds();
        scheduledEvents.executeCommands();
        xMain.shellLogic.console.ex("exec scripts/sv_checkgamestate");
        checkGameItems();
        updateEntityPositions(gameTimeMillis);
        uiInterface.tickReportSimulation = getTickReport();
    }

    private void checkGameItems() {
        HashMap<String, gThing> playerMap = xMain.shellLogic.serverScene.getThingMap("THING_PLAYER");
        HashMap<String, gThing> itemsMap = xMain.shellLogic.serverScene.getThingMap("THING_ITEM");
        Queue<gThing> playerQueue = new LinkedList<>();
        Queue<gThing> itemsQueue = new LinkedList<>();
        //TODO: fix concurrent modification by capturing a copy of the keyset and iterating over that instead
        ArrayList<String> keysetcopy = new ArrayList<>(itemsMap.keySet());
        for(String id : keysetcopy) {
            itemsQueue.add(itemsMap.get(id));
        }
        while(itemsQueue.size() > 0) {
            gItem item = (gItem) itemsQueue.remove();
            item.put("occupied", "0");
            for (String id : playerMap.keySet()) {
                playerQueue.add(playerMap.get(id));
            }
            while(playerQueue.size() > 0) {
                gPlayer player = (gPlayer) playerQueue.remove();
                if(player.containsFields(new String[]{"coordx", "coordy"}) && player.collidesWithThing(item))
                    item.activateItem(player);
            }
        }
    }


    private void updateEntityPositions(long gameTimeMillis) {
        nStateMap svMap = new nStateMap(cServerLogic.netServerThread.masterStateSnapshot);
        for(String id : svMap.keys()) {
            gPlayer obj = xMain.shellLogic.serverScene.getPlayerById(id);
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
                        obj.putInt("vel" + i, (Math.min(cClientLogic.velocityPlayerBase,
                                obj.getInt("vel" + i) + obj.getInt("accelrate"))));
                    }
                    else
                        obj.putInt("vel" + i, Math.max(0, obj.getInt("vel" + i) - obj.getInt("decelrate")));
                }
            }
            if(obj.wontClipOnMove(dx, obj.getInt("coordy"), xMain.shellLogic.serverScene))
                obj.putInt("coordx", dx);
            else {
                if(obj.getInt("vel2") > obj.getInt("vel3")) {
//                    obj.put("vel3", obj.get("vel2")); //bounce
                    obj.putInt("vel2", 0);
                }
                else {
//                    obj.put("vel2", obj.get("vel3")); //bounce
                    obj.putInt("vel3", 0);
                }
            }
            if(obj.wontClipOnMove(obj.getInt("coordx"), dy, xMain.shellLogic.serverScene))
                obj.putInt("coordy", dy);
            else {
                if(obj.getInt("vel0") > obj.getInt("vel1")) {
//                    obj.put("vel1", obj.get("vel0")); //bounce
                    obj.putInt("vel0", 0);
                }
                else {
//                    obj.put("vel0", obj.get("vel1")); //bounce
                    obj.putInt("vel1", 0);
                }
            }
        }

        try {
            HashMap<String, gThing> thingMap = xMain.shellLogic.serverScene.getThingMap("THING_BULLET");
            Queue<gThing> checkQueue = new LinkedList<>();
            String[] keys = thingMap.keySet().toArray(new String[0]);
            for (String id : keys) {
                checkQueue.add(thingMap.get(id));
            }
            while(checkQueue.size() > 0) {
                gBullet obj = (gBullet) checkQueue.remove();
                obj.putInt("coordx", obj.getInt("coordx")
                        - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
                obj.putInt("coordy", obj.getInt("coordy")
                        - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
            }
            checkBulletSplashes(gameTimeMillis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkBulletSplashes(long gameTimeMillis) {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap<String, gThing> bulletsMap = xMain.shellLogic.serverScene.getThingMap("THING_BULLET");
        nStateMap svMap = new nStateMap(cServerLogic.netServerThread.masterStateSnapshot);
        Queue<gThing> checkQueue = new LinkedList<>();
        String[] keys = bulletsMap.keySet().toArray(new String[0]);
        for (String id : keys) {
            checkQueue.add(bulletsMap.get(id));
        }
        while(checkQueue.size() > 0) {
            gBullet b = (gBullet) checkQueue.remove();
            if(gameTimeMillis - b.getLong("timestamp") > b.getInt("ttl")) {
                bulletsToRemoveIds.add(b.get("id"));
                //grenade explosion
                if(b.isInt("src", gWeapons.launcher))
                    pseeds.add(b);
                continue;
            }
            for(String blockId : xMain.shellLogic.serverScene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = xMain.shellLogic.serverScene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(b.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(b.get("id"));
                    if(b.isInt("src", gWeapons.launcher))
                        pseeds.add(b);
                }
            }
            for(String playerId : svMap.keys()) {
                gPlayer t = xMain.shellLogic.serverScene.getPlayerById(playerId);
                if(t != null && t.containsFields(new String[]{"coordx", "coordy"})
                        && b.collidesWithThing(t) && !b.get("srcid").equals(playerId)) {
                    bulletsToRemovePlayerMap.put(t, b);
                    if(b.isInt("src", gWeapons.launcher))
                        pseeds.add(b);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gBullet pseed : pseeds)
                gWeaponsLauncher.createGrenadeExplosion(pseed);
        }
        for(Object bulletId : bulletsToRemoveIds) {
            xMain.shellLogic.serverScene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            gBullet b = bulletsToRemovePlayerMap.get(p);
            //calculate dmg
            int dmg = b.getInt("dmg") - (int)((double)b.getInt("dmg")/2
                    *((Math.abs(Math.max(0, gTime.gameTime - b.getLong("timestamp"))
            )/(double)b.getInt("ttl")))); // dmg falloff based on age of bullet
            xMain.shellLogic.serverScene.getThingMap("THING_BULLET").remove(b.get("id"));
            //handle damage serverside
            xMain.shellLogic.console.ex(String.format("damageplayer %s %d %s", p.get("id"), dmg, b.get("srcid")));
            xMain.shellLogic.console.ex(String.format("spawnpopup %s %d", p.get("id"), dmg));
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        uiInterface.tickReportSimulation = 0;
    }
}
