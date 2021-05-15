import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class cClientLogic {
    static gScene scene = new gScene();

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
            xCon.ex("attack");
    }

    public static void gameLoop() {
        oDisplay.instance().checkDisplay();
        oAudio.instance().checkAudio();
        gCamera.updatePosition();
        if(sSettings.show_mapmaker_ui)
            selectThingUnderMouse();
        checkGameState();
        checkMovementStatus();
        checkColorStatus();
        if(getUserPlayer() != null) {
            pointPlayerAtMousePointer();
            checkPlayerFire();
        }
        checkFinishedAnimations();
        checkExpiredPopups();
        updateEntityPositions();
        gMessages.checkMessages();
    }

    public static synchronized void selectThingUnderMouse() {
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("itemid") && item.coordsWithinBounds(mc[0], mc[1])) {
                cVars.put("selecteditemid", item.get("itemid"));
                cVars.put("selecteditemname", item.get("type"));
                cVars.put("selectedprefabid", "");
                cVars.put("selectedprefabname", "");
                return;
            }
        }
        for(String id : scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = scene.getThingMap("THING_BLOCK").get(id);
            if(block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cVars.put("selectedprefabid", block.get("prefabid"));
                cVars.put("selectedprefabname", block.get("prefabname"));
                cVars.put("selecteditemid", "");
                cVars.put("selecteditemname", "");
                return;
            }
        }
        cVars.put("selectedprefabid", "");
        cVars.put("selecteditemid", "");
    }

    public static void updateEntityPositions() {
        for(String id : getPlayerIds()) {
            gPlayer obj = getPlayerById(id);
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "accelrate"};
            //check null fields
            if(!obj.containsFields(requiredFields))
                break;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < System.currentTimeMillis()) {
                obj.putLong("acceltick", System.currentTimeMillis()+obj.getInt("accelrate"));
                for (int i = 0; i < 4; i++) {
                    //user player
                    if(isUserPlayer(obj)) {
                        if (obj.getInt("mov"+i) > 0) {
                            obj.putInt("vel" + i, (Math.min(cVars.getInt("velocityplayer"),
                                    obj.getInt("vel" + i) + 1)));
                        }
                        else
                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
                    }
                }
            }
            if(dx != obj.getInt("coordx") && obj.wontClipOnMove(0,dx, scene)) {
                obj.putInt("coordx", dx);
            }
            if(dy != obj.getInt("coordy") && obj.wontClipOnMove(1,dy, scene)) {
                obj.putInt("coordy", dy);
            }
        }

        HashMap bulletsMap = scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet obj = (gBullet) bulletsMap.get(id);
            obj.putInt("coordx", obj.getInt("coordx")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                    - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }
        HashMap popupsMap = scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup obj = (gPopup) popupsMap.get(id);
            obj.put("coordx", Integer.toString(obj.getInt("coordx")
                    - (int) (cVars.getInt("velocitypopup")*Math.cos(obj.getDouble("fv")+Math.PI/2))));
            obj.put("coordy", Integer.toString(obj.getInt("coordy")
                    - (int) (cVars.getInt("velocitypopup")*Math.sin(obj.getDouble("fv")+Math.PI/2))));
        }
        checkBulletSplashes();
    }

    public static void checkBulletSplashes() {
        ArrayList bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap bulletsMap = scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet b = (gBullet) bulletsMap.get(id);
            if(System.currentTimeMillis()-b.getLong("timestamp") > b.getInt("ttl")){
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
            for(String playerId : getPlayerIds()) {
                gPlayer t = getPlayerById(playerId);
                if(t != null && t.containsFields(new String[]{"coordx", "coordy"})
                        && b.doesCollideWithPlayer(t) && !b.get("srcid").equals(playerId)) {
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
            cClientLogic.playPlayerDeathSound();
            scene.getThingMap("THING_BULLET").remove(bulletsToRemovePlayerMap.get(p).get("id"));
        }
    }

    public static void checkFinishedAnimations() {
        ArrayList<String> animationIdsToRemove = new ArrayList<>();
        //remove finished animations
        HashMap animationMap = scene.getThingMap("THING_ANIMATION");
        for(Object id : animationMap.keySet()) {
            gAnimationEmitter a = (gAnimationEmitter) animationMap.get(id);
            if(a.getInt("frame") > gAnimations.animation_selection[a.getInt("animation")].frames.length) {
                animationIdsToRemove.add((String) id);
            }
        }
        for(String aid : animationIdsToRemove) {
            scene.getThingMap("THING_ANIMATION").remove(aid);
        }
    }

    public static void checkExpiredPopups() {
        String popupIdToRemove = "";
        HashMap popupsMap = scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup g = (gPopup) popupsMap.get(id);
            if(g.getLong("timestamp") < System.currentTimeMillis() - cVars.getInt("popuplivetime")) {
                popupIdToRemove = (String) id;
                break;
            }
        }
        if(popupIdToRemove.length() > 0) {
            popupsMap.remove(popupIdToRemove);
        }
    }

    public static void changeWeapon(int newweapon) {
        gPlayer p = cClientLogic.getUserPlayer();
        if(p != null) {
            if(newweapon != p.getInt("weapon"))
                xCon.ex("playsound sounds/grenpinpull.wav");
            p.putInt("weapon", newweapon);
            cClientLogic.getUserPlayer().checkSpriteFlip();
        }
    }

    public static void playPlayerDeathSound() {
        double r = Math.random();
        if(r > .99)
            xCon.ex("playsound sounds/growl.wav");
        else if(r > .49)
            xCon.ex("playsound sounds/shout.wav");
        else
            xCon.ex("playsound sounds/death.wav");
    }

    public static void checkColorStatus() {
        //check all id colors, including yours
        for(String id : nClient.instance().serverArgsMap.keySet()) {
            gPlayer p = getPlayerById(id);
            String ccol = nClient.instance().serverArgsMap.get(id).get("color");
            if(p == null || ccol == null)
                continue;
            if(!p.get("pathsprite").contains(ccol)) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", ccol,
                        p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
            }
        }
    }

    public static void pointPlayerAtMousePointer() {
        gPlayer p = getUserPlayer();
        int[] mc = uiInterface.getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.getInt("coordx") + p.getInt("dimw")/2
                - cVars.getInt("camx"));
        double dy = mc[1] - eUtils.scaleInt(p.getInt("coordy") + p.getInt("dimh")/2
                - cVars.getInt("camy"));
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.putDouble("fv", angle);
        p.checkSpriteFlip();
    }

    //clientside prediction for movement aka smoothing
    public static void checkMovementStatus() {
        //other players
        for(String id : getPlayerIds()) {
            if(id.equals(uiInterface.uuid) || !nClient.instance().serverArgsMap.containsKey(id))
                continue;
            gPlayer obj = getPlayerById(id);
            for (int i = 0; i < 4; i++) {
                if(nClient.instance().serverArgsMap.get(id).containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(nClient.instance().serverArgsMap.get(
                            obj.get("id")).get("vels").split("-")[i]));
            }
        }
        for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                String[] requiredFields = new String[]{"fv", "x", "y"};
                if(!nClient.instance().containsArgsForId(id, requiredFields))
                    continue;
                HashMap<String, String> cargs = nClient.instance().serverArgsMap.get(id);
                double cfv = Double.parseDouble(cargs.get("fv"));
                gPlayer p = getPlayerById(id);
                if(p == null)
                    return;
                if(sVars.isZero("smoothing")) {
                    p.put("coordx", cargs.get("x"));
                    p.put("coordy", cargs.get("y"));
                }
                if(p.getDouble("fv") != cfv) {
                    p.putDouble("fv", cfv);
                    p.checkSpriteFlip();
                }
            }
        }
    }

    public static gPlayer getPlayerById(String id) {
        return (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
    }

    static void checkGameState() {
        if(nClient.instance().serverArgsMap.containsKey("server")
                && nClient.instance().serverArgsMap.get("server").containsKey("flagmasterid")
                && nClient.instance().serverArgsMap.get("server").get("flagmasterid").length() > 0) {
            //check to delete flags that should not be present anymore
            if (scene.getThingMap("ITEM_FLAG").size() > 0)
                xCon.ex("clearthingmap ITEM_FLAG");
        }
    }
}
