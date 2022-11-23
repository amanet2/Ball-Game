import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class cClientLogic {
    static int maxhp = 500;
    static double volume = 100.0;
    static String selecteditemid = "";
    static String selectedPrefabId = "";
    static int[] weaponStocks = {0, 30, 30, 30, 30, 0};
    static String playerName = "player";
    static String playerColor = "blue";
    static int velocityPlayer = 8;
    static boolean debug = false;
    static boolean debuglog = false;
    static String newprefabname = "room";
    static int gamemode = 0;
    static String gamemodeTitle = "Rock Master";
    static String gamemodeText = "Rock Other Players";
    static boolean maploaded = false;
    static int prevX = 0;
    static int prevY = 0;
    static int prevW = 300;
    static int prevH = 300;
    static gScene scene;
    static gTimeEventSet timedEvents = new gTimeEventSet();
    static long serverSendTime = 0;
    static long serverRcvTime = 0;
    static int ping = 0;

    public static gPlayer getUserPlayer() {
        return scene.getPlayerById(uiInterface.uuid);
    }

    public static Collection<String> getPlayerIds() {
        return scene.getThingMap("THING_PLAYER").keySet();
    }

    public static boolean isUserPlayer(gPlayer player) {
        return player.isVal("id", uiInterface.uuid);
    }

    public static void checkPlayerFire() {
        if(getUserPlayer() != null && iMouse.holdingMouseLeft)
            xCon.ex(String.format("exec scripts/attack %d",
                    (long)gWeapons.fromCode(cClientLogic.getUserPlayer().getInt("weapon")).refiredelay));
    }

    public static void gameLoop(long loopTimeMillis) {
        cClientVars.instance().put("gametimemillis", Long.toString(loopTimeMillis));
        timedEvents.executeCommands();
        if(oDisplay.instance().frame.isVisible()) {
            if(sSettings.show_mapmaker_ui)
                cClientLogic.selectThingUnderMouse();
            if(getUserPlayer() != null)
                pointPlayerAtMousePointer();
        }
        oAudio.instance().checkAudio();
        gCamera.updatePosition();
        if(getUserPlayer() != null)
            checkPlayerFire();
        updateEntityPositions(loopTimeMillis);
        gMessages.checkMessages();
    }

    public static synchronized void selectThingUnderMouse() {
        if(!cClientLogic.maploaded)
            return;
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.coordsWithinBounds(mc[0], mc[1])) {
                selecteditemid = item.get("id");
                selectedPrefabId = "";
                return;
            }
        }
        for(String id : scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = scene.getThingMap("THING_BLOCK").get(id);
            if(!block.get("type").equals("BLOCK_FLOOR")
                    && block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                selectedPrefabId = block.get("prefabid");
                selecteditemid = "";
                return;
            }
        }
        for(String id : scene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing block = scene.getThingMap("BLOCK_FLOOR").get(id);
            if(block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                selectedPrefabId = block.get("prefabid");
                selecteditemid = "";
                return;
            }
        }
        selectedPrefabId = "";
        selecteditemid = "";
    }

    public static void updateEntityPositions(long gameTimeMillis) {
        for(String id : getPlayerIds()) {
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
            if(obj.getLong("acceltick") < gameTimeMillis) {
                obj.putLong("acceltick", gameTimeMillis + obj.getInt("accelrate"));
                for (int i = 0; i < 4; i++) {
                    //user player
                    if(isUserPlayer(obj)) {
                        if (obj.getInt("mov"+i) > 0) {
                            obj.putInt("vel" + i, (Math.min(cClientLogic.velocityPlayer,
                                    obj.getInt("vel" + i) + 1)));
                        }
                        else
                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
                    }
                }
            }
            if(obj.wontClipOnMove(0,dx, scene)) {
                obj.putInt("coordx", dx);
                if(isUserPlayer(obj))
                    gCamera.setX(dx - eUtils.unscaleInt(sSettings.width/2));
            }
            if(obj.wontClipOnMove(1,dy, scene)) {
                obj.putInt("coordy", dy);
                if(isUserPlayer(obj))
                    gCamera.setY(dy - eUtils.unscaleInt(sSettings.height/2));
            }
        }

        Collection<String> bulletcoll = scene.getThingMap("THING_BULLET").keySet();
        int bsize = bulletcoll.size();
        String[] bids = bulletcoll.toArray(new String[bsize]);
        for(String id : bids) {
            gBullet obj = (gBullet) scene.getThingMap("THING_BULLET").get(id);
            if(obj == null)
                continue;
            obj.putInt("coordx", obj.getInt("coordx")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }

        Collection<String> pColl = scene.getThingMap("THING_POPUP").keySet();
        int psize = pColl.size();
        String[] pids = pColl.toArray(new String[psize]);
        for(String id : pids) {
            gPopup obj = (gPopup) scene.getThingMap("THING_POPUP").get(id);
            obj.put("coordx", Integer.toString(obj.getInt("coordx")
                    - (int) (sSettings.velocity_popup*Math.cos(obj.getDouble("fv")+Math.PI/2))));
            obj.put("coordy", Integer.toString(obj.getInt("coordy")
                    - (int) (sSettings.velocity_popup*Math.sin(obj.getDouble("fv")+Math.PI/2))));
        }
        checkBulletSplashes(gameTimeMillis);
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
            for(String blockId : scene.getThingMapIds("BLOCK_COLLISION")) {
                gThing bl = scene.getThingMap("BLOCK_COLLISION").get(blockId);
                if(b.collidesWithThing(bl)) {
                    bulletsToRemoveIds.add(b.get("id"));
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
            for(String playerId : getPlayerIds()) {
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
            scene.getThingMap("THING_BULLET").remove(bulletsToRemovePlayerMap.get(p).get("id"));
        }
    }

    public static void pointPlayerAtMousePointer() {
        gPlayer p = getUserPlayer();
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

    public static gPlayer getPlayerById(String id) {
        if(!scene.getThingMap("THING_PLAYER").containsKey(id))
            return null;
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }

    public static int getNewItemId() {
        int itemId = 0;
        for(String id : scene.getThingMap("THING_ITEM").keySet()) {
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        return itemId+1; //want to be the _next_ id
    }
}
