import java.awt.*;
import java.util.*;

public class cScripts {
    public static void checkBulletSplashes() {
        ArrayList bulletsToRemoveIds = new ArrayList<>();
        ArrayList<String> animationIdsToRemove = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        String popupIdToRemove = "";
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap bulletsMap = eManager.currentMap.scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet b = (gBullet) bulletsMap.get(id);
            if(System.currentTimeMillis()-b.getLong("timestamp") > b.getInt("ttl")){
                bulletsToRemoveIds.add(id);
                if (sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1) {
                    eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                            cScripts.createId(), new gAnimationEmitter(b.getInt("anim"),
                                    b.getInt("coordx"), b.getInt("coordy")));
                }
                //grenade explosion
                if(b.isInt("src", gWeapons.type.LAUNCHER.code())) {
                    pseeds.add(b);
                }
                continue;
            }
            for(String playerId : gScene.getPlayerIds()) {
                gPlayer t = gScene.getPlayerById(playerId);
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
            eManager.currentMap.scene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            playPlayerDeathSound();
            createDamagePopup(p, bulletsToRemovePlayerMap.get(p));
        }
        HashMap popupsMap = eManager.currentMap.scene.getThingMap("THING_POPUP");
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
        //remove finished animations
        HashMap animationMap = eManager.currentMap.scene.getThingMap("THING_ANIMATION");
        for(Object id : animationMap.keySet()) {
            gAnimationEmitter a = (gAnimationEmitter) animationMap.get(id);
            if(a.getInt("frame") > gAnimations.animation_selection[a.getInt("animation")].frames.length) {
                animationIdsToRemove.add((String) id);
            }
        }
        for(String aid : animationIdsToRemove) {
            eManager.currentMap.scene.getThingMap("THING_ANIMATION").remove(aid);
        }
    }

    public static String createId() {
        int min = 11111111;
        int max = 99999999;
        int idInt = new Random().nextInt(max - min + 1) + min;
        return Integer.toString(idInt);
    }

    public static String createBotId() {
        int min = 11111;
        int max = 99999;
        int idInt = new Random().nextInt(max - min + 1) + min;
        return Integer.toString(idInt);
    }

    //call this everytime a bullet intersects a player
    public static void createDamagePopup(gPlayer dmgvictim, gBullet bullet) {
        //get shooter details
        String killerid = bullet.get("srcid");
        //calculate dmg
        int adjusteddmg = bullet.getInt("dmg") - (int)((double)bullet.getInt("dmg")/2
                *((Math.abs(System.currentTimeMillis() - bullet.getLong("timestamp")
        )/(double)bullet.getInt("ttl"))));
        //play animations on all clients
        eManager.currentMap.scene.getThingMap("THING_POPUP").put(cScripts.createId(),
                new gPopup(dmgvictim.getInt("coordx") + (int)(Math.random()*(dmgvictim.getInt("dimw")+1)),
                        dmgvictim.getInt("coordy") + (int)(Math.random()*(dmgvictim.getInt("dimh")+1)),
                        Integer.toString(adjusteddmg), 0.0));
        if(sVars.isOne("vfxenableanimations") && bullet.getInt("anim") > -1)
            eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                    createId(), new gAnimationEmitter(gAnimations.ANIM_SPLASH_RED,
                            bullet.getInt("coordx"), bullet.getInt("coordy")));
        eManager.currentMap.scene.getThingMap("THING_BULLET").remove(bullet.get("id"));
        //handle damage serverside
        if(sSettings.IS_SERVER) {
            String cmdString = "damageplayer " + dmgvictim.get("id") + " " + adjusteddmg + " " + killerid;
            nServer.instance().addNetCmd("server", cmdString);
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

    public static void changeWeapon(int newweapon) {
        gPlayer p = gClientLogic.getUserPlayer();
        if(p != null) {
            if(newweapon != p.getInt("weapon"))
                xCon.ex("playsound sounds/grenpinpull.wav");
            p.putInt("weapon", newweapon);
            gClientLogic.getUserPlayer().checkSpriteFlip();
        }
    }
}
