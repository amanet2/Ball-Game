import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class eGameLogicClient implements eGameLogic {
    private int ticks = 0;
    private long frameCounterTime = -1;
    private long tickCounterTime = -1;

    public eGameLogicClient() {

    }

    @Override
    public void init() {
        oDisplay.instance().showFrame();
        gCamera.init();
        gAnimations.init();
        xCon.ex("exec config/itemsdef");
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
            nClient.instance().processPackets();
            cClientVars.instance().put("gametimemillis", Long.toString(gameTimeMillis));
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
            gMessages.checkMessages();
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
        double mod = (double)sSettings.rateserver/(double)sSettings.rategame;
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
                            obj.putInt("vel" + i, (Math.min(cClientLogic.velocityPlayer,
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

        Collection<String> bulletcoll = cClientLogic.scene.getThingMap("THING_BULLET").keySet();
        int bsize = bulletcoll.size();
        String[] bids = bulletcoll.toArray(new String[bsize]);
        for(String id : bids) {
            gBullet obj = (gBullet) cClientLogic.scene.getThingMap("THING_BULLET").get(id);
            if(obj == null)
                continue;
            obj.putInt("coordx", obj.getInt("coordx")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }

        Collection<String> pColl = cClientLogic.scene.getThingMap("THING_POPUP").keySet();
        int psize = pColl.size();
        String[] pids = pColl.toArray(new String[psize]);
        for(String id : pids) {
            gPopup obj = (gPopup) cClientLogic.scene.getThingMap("THING_POPUP").get(id);
            obj.put("coordx", Integer.toString(obj.getInt("coordx")
                    - (int) (sSettings.velocity_popup*Math.cos(obj.getDouble("fv")+Math.PI/2))));
            obj.put("coordy", Integer.toString(obj.getInt("coordy")
                    - (int) (sSettings.velocity_popup*Math.sin(obj.getDouble("fv")+Math.PI/2))));
        }
        checkBulletSplashes(gameTimeMillis);
    }

    private void checkBulletSplashes(long gameTimeMillis) {
        ArrayList<String> bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap<String, gThing> bulletsMap = cClientLogic.scene.getThingMap("THING_BULLET");
        for(String id : bulletsMap.keySet()) {
            gBullet b = (gBullet) bulletsMap.get(id);
            if(gameTimeMillis - b.getLong("timestamp") > b.getInt("ttl")){
                bulletsToRemoveIds.add(id);
//                if (sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1) {
//                    currentMap.scene.getThingMap("THING_ANIMATION").put(
//                            createId(), new gAnimationEmitter(b.getInt("anim"),
//                                    b.getInt("coordx"), b.getInt("coordy")));
//                }
                //grenade explosion
                if(b.isInt("src", gWeapons.type.LAUNCHER.code())) {
                    pseeds.add(b);
                }
                continue;
            }
            for(String blockId : cClientLogic.scene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = cClientLogic.scene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(b.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(b.get("id"));
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
            for(String playerId : cClientLogic.getPlayerIds()) {
                gPlayer t = cClientLogic.getPlayerById(playerId);
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
                    xCon.ex("cl_addcom fireweapon " + uiInterface.uuid + " " + weapint);
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

    }
}
