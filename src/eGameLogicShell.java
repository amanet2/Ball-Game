import java.util.*;

public class eGameLogicShell implements eGameLogic {
    private int ticks = 0;
    private long frameCounterTime = -1;
    private long tickCounterTime = -1;
    private eGameSession parentSession;

    public eGameLogicShell() {
        if(sSettings.show_mapmaker_ui) {
            sSettings.drawhitboxes = true;
            sSettings.drawmapmakergrid = true;
            sSettings.zoomLevel = 0.5;
        }
    }

    public void setParentSession(eGameSession session) {
        parentSession = session;
    }

    @Override
    public void init() {
        oDisplay.instance().showFrame();
        gCamera.init();
        gAnimations.init();
    }

    @Override
    public void input() {
        gTime.gameTime = System.currentTimeMillis();
        iInput.readKeyInputs();
    }

    @Override
    public void update() {
        long gameTimeMillis = gTime.gameTime;
        if(sSettings.IS_CLIENT) {
            cClientLogic.netClientThread.processPackets();
            cClientLogic.vars.put("gametimemillis", Long.toString(gameTimeMillis));
            cClientLogic.timedEvents.executeCommands();
            if(oDisplay.instance().frame.isVisible()) {
                if(cClientLogic.getUserPlayer() != null)
                   pointPlayerAtMousePointer();
                else if(sSettings.show_mapmaker_ui)
                    selectThingUnderMouse();
            }
            oAudio.instance().checkAudio(); //setting to mute game when not in focus?
            if(cClientLogic.getUserPlayer() != null)
                checkPlayerFire();
            updateEntityPositions(gameTimeMillis);
        }
        ticks += 1;
        if(tickCounterTime < gameTimeMillis) {
            uiInterface.tickReport = ticks;
            ticks = 0;
            tickCounterTime = gameTimeMillis + 1000;
        }
    }

    private void updateEntityPositions(long gameTimeMillis) {
        if(sSettings.show_mapmaker_ui && cClientLogic.getUserPlayer() == null)
            gCamera.updatePosition();
        double mod = (double)sSettings.rateserver/(double)sSettings.rateShell;
        for(String id : cClientLogic.getPlayerIds()) {
            gPlayer obj = cClientLogic.getPlayerById(id);
            if(obj == null)
                continue;
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "acceldelay", "accelrate",
                    "decelrate"
            };
            //check null fields
            if(!obj.containsFields(requiredFields))
                continue;
            int mx = obj.getInt("vel3") - obj.getInt("vel2");
            int my = obj.getInt("vel1") - obj.getInt("vel0");
            int dx = obj.getInt("coordx") + (int)(mx*mod);
            int dy = obj.getInt("coordy") + (int)(my*mod);
            if(obj.getLong("acceltick") < gameTimeMillis) {
                obj.putLong("acceltick", gameTimeMillis + obj.getInt("acceldelay"));
                //user player
                if(isUserPlayer(obj)) {
                    for (int i = 0; i < 4; i++) {
                        if (obj.getInt("mov" + i) > 0)
                            obj.putInt("vel" + i, (Math.min(cClientLogic.velocityPlayerBase,
                                    obj.getInt("vel" + i) + obj.getInt("accelrate"))));
                        else
                            obj.putInt("vel" + i, Math.max(0, obj.getInt("vel" + i) - obj.getInt("decelrate")));
                    }
                }
            }
            if(!obj.wontClipOnMove(dx, obj.getInt("coordy"), cClientLogic.scene))
                dx = obj.getInt("coordx");
            if(!obj.wontClipOnMove(obj.getInt("coordx"), dy, cClientLogic.scene))
                dy = obj.getInt("coordy");
            if(isUserPlayer(obj))
                gCamera.put("coords", dx + ":" + dy);
            obj.putInt("coordx", dx);
            obj.putInt("coordy", dy);
        }
        try {
            HashMap<String, gThing> thingMap = cClientLogic.scene.getThingMap("THING_BULLET");
            Queue<gThing> checkQueue = new LinkedList<>();
            String[] keys = thingMap.keySet().toArray(new String[0]);
            for (String id : keys) {
                checkQueue.add(thingMap.get(id));
            }
            while (checkQueue.size() > 0) {
                gBullet obj = (gBullet) checkQueue.remove();
                obj.putInt("coordx", obj.getInt("coordx")
                        - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel * Math.cos(obj.getDouble("fv") + Math.PI / 2)));
                obj.putInt("coordy", obj.getInt("coordy")
                        - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel * Math.sin(obj.getDouble("fv") + Math.PI / 2)));
            }
            checkBulletSplashes(gameTimeMillis);
            //popups
            thingMap = cClientLogic.scene.getThingMap("THING_POPUP");
            checkQueue = new LinkedList<>();
            for (String id : thingMap.keySet()) {
                checkQueue.add(thingMap.get(id));
            }
            while (checkQueue.size() > 0) {
                gPopup obj = (gPopup) checkQueue.remove();
                obj.put("coordx", Integer.toString(obj.getInt("coordx")
                        - (int) (sSettings.velocity_popup * Math.cos(obj.getDouble("fv") + Math.PI / 2))));
                obj.put("coordy", Integer.toString(obj.getInt("coordy")
                        - (int) (sSettings.velocity_popup * Math.sin(obj.getDouble("fv") + Math.PI / 2))));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBulletSplashes(long gameTimeMillis) {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap<String, gThing> bulletsMap = cClientLogic.scene.getThingMap("THING_BULLET");
        Queue<gThing> checkThings = new LinkedList<>();
        String[] keys = bulletsMap.keySet().toArray(new String[0]);
        for (String id : keys) {
            checkThings.add(bulletsMap.get(id));
        }
        while (checkThings.size() > 0) {
            gBullet t = (gBullet) checkThings.remove();
            if(gameTimeMillis - t.getLong("timestamp") > t.getInt("ttl")){
                bulletsToRemoveIds.add(t.get("id"));
//                if (sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1) {
//                    currentMap.scene.getThingMap("THING_ANIMATION").put(
//                            createId(), new gAnimationEmitter(b.getInt("anim"),
//                                    b.getInt("coordx"), b.getInt("coordy")));
//                }
                //grenade explosion
                if(t.isInt("src", gWeapons.launcher)) {
                    pseeds.add(t);
                }
                continue;
            }
            for(String blockId : cClientLogic.scene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = cClientLogic.scene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(t.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(t.get("id"));
                    if(t.isInt("src", gWeapons.launcher))
                        pseeds.add(t);
                }
            }
            for(String playerId : cClientLogic.getPlayerIds()) {
                gPlayer p = cClientLogic.getPlayerById(playerId);
                if(p != null && p.containsFields(new String[]{"coordx", "coordy"})
                        && t.collidesWithThing(p) && !t.get("srcid").equals(playerId)) {
                    bulletsToRemovePlayerMap.put(p, t);
                    if(t.isInt("src", gWeapons.launcher))
                        pseeds.add(t);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gBullet pseed : pseeds)
                gWeaponsLauncher.createGrenadeExplosion(pseed);
        }
        for(Object bulletId : bulletsToRemoveIds) {
            cClientLogic.scene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            cClientLogic.scene.getThingMap("THING_BULLET").remove(bulletsToRemovePlayerMap.get(p).get("id"));
        }
    }

    private void selectThingUnderMouse() {
        if(!cClientLogic.maploaded)
            return;
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selecteditemid = item.get("id");
                cClientLogic.selectedPrefabId = "";
                return;
            }
        }
        for(String id : cClientLogic.scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
            if(!block.get("type").equals("BLOCK_FLOOR")
                    && block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selectedPrefabId = block.get("prefabid");
                cClientLogic.selecteditemid = "";
                return;
            }
        }
        for(String id : cClientLogic.scene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing block = cClientLogic.scene.getThingMap("BLOCK_FLOOR").get(id);
            if(block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cClientLogic.selectedPrefabId = block.get("prefabid");
                cClientLogic.selecteditemid = "";
                return;
            }
        }
        cClientLogic.selectedPrefabId = "";
        cClientLogic.selecteditemid = "";
    }

    private boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", uiInterface.uuid);
    }

    private void checkPlayerFire() {
        if(cClientLogic.getUserPlayer() != null && iMouse.holdingMouseLeft) {
            gPlayer player = cClientLogic.getUserPlayer();
            if(player.contains("cooldown")) {
                int weapint = player.getInt("weapon");
                long gametimemillis = gTime.gameTime;
                if(player.getLong("cooldown") <= gametimemillis) {
                    cClientLogic.netClientThread.addNetCmd(String.format("fireweapon %s %d", uiInterface.uuid, weapint));
                    player.putLong("cooldown", gametimemillis + gWeapons.fromCode(weapint).refiredelay);
                }
            }
        }
    }

    private void pointPlayerAtMousePointer() {
        gPlayer p = cClientLogic.getUserPlayer();
        int[] mc = uiInterface.getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.getInt("coordx") + p.getInt("dimw")/2 - gCamera.getX());
        double dy = mc[1] - eUtils.scaleInt(p.getInt("coordy") + p.getInt("dimh")/2 - gCamera.getY());
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.putDouble("fv", angle);
        p.checkSpriteFlip();
    }

    @Override
    public void render() {
        oDisplay.instance().frame.repaint();
        long gameTimeMillis = gTime.gameTime;
        if (frameCounterTime < gameTimeMillis) {
            uiInterface.fpsReport = uiInterface.frames;
            uiInterface.frames = 0;
            frameCounterTime = gameTimeMillis + 1000;
        }
    }

    @Override
    public void cleanup() {
        cServerLogic.vars.saveToFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        cClientLogic.vars.saveToFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        if(cClientLogic.debuglog)
            xCon.instance().saveLog(sSettings.CONSOLE_LOG_LOCATION);
        System.exit(0);
    }

    @Override
    public void disconnect() {
        parentSession.destroy();
    }
}
