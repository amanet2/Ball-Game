import java.awt.*;
import java.util.*;

public class cScripts {
    public static void centerCamera() {
        gThing p = gScene.getPlayerById(cVars.get("camplayertrackingid"));
        if(p == null)
            p = cGameLogic.userPlayer();
        if(p != null) {
            cVars.putInt("cammode", gCamera.MODE_TRACKING);
            cVars.putInt("camx",
                    ((p.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2) + p.getInt("dimw")/2));
            cVars.putInt("camy",
                    ((p.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2) + p.getInt("dimh")/2));
        }
    }

    public static void pointPlayerAtMousePointer() {
        gPlayer p = cGameLogic.userPlayer();
        int[] mc = getMouseCoordinates();
        double dx = mc[0] - eUtils.scaleInt(p.getInt("coordx") + p.getInt("dimw")/2
                - cVars.getInt("camx"));
        double dy = mc[1] - eUtils.scaleInt(p.getInt("coordy") + p.getInt("dimh")/2
                - cVars.getInt("camy"));
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.putDouble("fv", angle);
        checkPlayerSpriteFlip(p);
    }

    public static void pointPlayerAtCoords(gPlayer p, int x, int y) {
        double dx = x - p.getInt("coordx") + p.getInt("dimw")/2;
        double dy = y - p.getInt("coordy") + p.getInt("dimh")/2;
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        p.putDouble("fv", angle);
        checkPlayerSpriteFlip(p);
    }

    public static int[] getMouseCoordinates() {
        return new int[]{
            MouseInfo.getPointerInfo().getLocation().x - oDisplay.instance().frame.getLocationOnScreen().x
                - oDisplay.instance().getContentPaneOffsetDimension()[0],
            MouseInfo.getPointerInfo().getLocation().y - oDisplay.instance().frame.getLocationOnScreen().y
                - oDisplay.instance().getContentPaneOffsetDimension()[1]
        };
    }

    public static void checkPlayerSpriteFlip(gPlayer p) {
        if(p == null)
            return;
        if((p.getDouble("fv") >= 7*Math.PI/4 && p.getDouble("fv") <= 9*Math.PI/4)) {
            if(!p.get("pathsprite").contains("a00")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a00.png", p.get("color"))));
            }
        }
        else if((p.getDouble("fv") <= 3*Math.PI/4)
            || (p.getDouble("fv") >= 2*Math.PI || p.getDouble("fv") <= 3*Math.PI/4)) {
            if(!p.get("pathsprite").contains("a03")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a03.png",p.get("color"))));
                String sprite = p.isInt("weapon", gWeapons.type.AUTORIFLE.code()) ? "misc/autorifle.png" :
                    p.isInt("weapon", gWeapons.type.SHOTGUN.code()) ? "misc/shotgun.png" :
                        p.isInt("weapon", gWeapons.type.GLOVES.code()) ? "misc/glove.png" :
                        p.isInt("weapon", gWeapons.type.NONE.code()) ? "" :
                        p.isInt("weapon", gWeapons.type.LAUNCHER.code()) ? "misc/launcher.png" :
                            "misc/bfg.png";
                gWeapons.fromCode(p.getInt("weapon")).dims[1] =
                    gWeapons.fromCode(p.getInt("weapon")).flipdimr;
                gWeapons.fromCode(p.getInt("weapon")).setSpriteFromPath(eUtils.getPath(sprite));
            }
        }
        else if(p.getDouble("fv") <= 5*Math.PI/4) {
            if(!p.get("pathsprite").contains("a04")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a04.png",p.get("color"))));
            }
        }
        else {
            if(!p.get("pathsprite").contains("a05")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a05.png",p.get("color"))));
                String sprite = p.isInt("weapon", gWeapons.type.AUTORIFLE.code()) ? "misc/autorifle_flip.png" :
                    p.isInt("weapon", gWeapons.type.SHOTGUN.code()) ? "misc/shotgun_flip.png" :
                    p.isInt("weapon", gWeapons.type.GLOVES.code()) ? "misc/glove_flip.png" :
                    p.isInt("weapon", gWeapons.type.NONE.code()) ? "" :
                    p.isInt("weapon", gWeapons.type.LAUNCHER.code()) ? "misc/launcher_flip.png" :
                        "misc/bfg_flip.png";
                if(gWeapons.fromCode(p.getInt("weapon")) != null) {
                    gWeapons.fromCode(p.getInt("weapon")).dims[1] =
                            gWeapons.fromCode(p.getInt("weapon")).flipdiml;
                    gWeapons.fromCode(p.getInt("weapon")).setSpriteFromPath(eUtils.getPath(sprite));
                }
            }
        }
    }

    public static synchronized void getUIMenuItemUnderMouse() {
        if(cVars.isZero("blockmouseui")) {
            int[] mc = getMouseCoordinates();
            int[] xBounds = new int[]{0, sSettings.width / 4};
            int[] yBounds = sVars.getInt("displaymode") > 0
                    ? new int[]{14 * sSettings.height / 16, 15 * sSettings.height / 16}
                    : new int[]{15 * sSettings.height / 16, sSettings.height};
            if ((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                if (!uiMenus.gobackSelected) {
                    uiMenus.gobackSelected = true;
                    uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
                }
                return;
            } else
                uiMenus.gobackSelected = false;
            if (uiMenus.selectedMenu != uiMenus.MENU_CONTROLS) {
                for (int i = 0; i < uiMenus.menuSelection[uiMenus.selectedMenu].items.length; i++) {
                    xBounds = new int[]{sSettings.width / 2 - sSettings.width / 8,
                            sSettings.width / 2 + sSettings.width / 8};
                    yBounds = new int[]{11 * sSettings.height / 30 + i * sSettings.height / 30,
                            11 * sSettings.height / 30 + (i + 1) * sSettings.height / 30};
                    if (sVars.isIntVal("displaymode", oDisplay.displaymode_windowed)) {
                        yBounds[0] += 40;
                        yBounds[1] += 40;
                    }
                    if ((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                        if (uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem != i)
                            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = i;
                        return;
                    }
                }
            }
            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
        }
    }

    public static synchronized void selectThingUnderMouse() {
        int[] mc = getMouseCoordinates();
        for(String id : eManager.currentMap.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = eManager.currentMap.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("itemid") && item.coordsWithinBounds(mc[0], mc[1])) {
                cVars.put("selecteditemid", item.get("itemid"));
                cVars.put("selecteditemname", item.get("type"));
                cVars.put("selectedprefabid", "");
                cVars.put("selectedprefabname", "");
//                cEditorLogic.state.createObjCode = gScene.THING_ITEM;
                return;
            }
        }
        for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
            if(block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cVars.put("selectedprefabid", block.get("prefabid"));
                cVars.put("selectedprefabname", block.get("prefabname"));
                cVars.put("selecteditemid", "");
                cVars.put("selecteditemname", "");
//                cEditorLogic.state.createObjCode = gScene.THING_PREFAB;
                return;
            }
        }
        cVars.put("selectedprefabid", "");
        cVars.put("selecteditemid", "");
    }

    public static void processOptionText(String optionTitle, String enteredText) {
        if("New Name".contains(optionTitle)) {
            sVars.put("playername", enteredText);
            uiMenus.menuSelection[uiMenus.MENU_PROFILE].items[0].text =
                    String.format("Name [%s]", sVars.get("playername"));
        }
        else if("New IP".contains(optionTitle)) {
            sVars.put("joinip", enteredText);
            uiMenus.menuSelection[uiMenus.MENU_JOINGAME].items[1].text =
                String.format("Server IP [%s]", sVars.get("joinip"));
        }
        else if("New Port".contains(optionTitle)) {
            sVars.put("joinport", enteredText);
            uiMenus.menuSelection[uiMenus.MENU_JOINGAME].items[2].text =
                String.format("Server Port [%s]", sVars.get("joinport"));
        }
        else if("New Score Limit".contains(optionTitle)) {
            sVars.put("scorelimit", enteredText);
            uiMenus.menuSelection[uiMenus.MENU_NEWGAME].items[2].text =
                String.format("Score Limit [%s]", sVars.get("scorelimit"));
        }
        else if("New Time Limit".contains(optionTitle)) {
            sVars.put("timelimit", enteredText);
            uiMenus.menuSelection[uiMenus.MENU_NEWGAME].items[3].text =
                String.format("Time Limit [%s]", sVars.get("timelimit"));
        }
    }

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
            for(gTile t : eManager.currentMap.scene.tiles()) {
                if((b.doesCollideWithinTile(t) || b.doesCollideWithinCornerTile(t))
                        && b.getInt("src") != gWeapons.type.GLOVES.code()
                && b.isZero("isexplosionpart")) {
                    bulletsToRemoveIds.add(id);
                    if (sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1) {
                        eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                                cScripts.createId(), new gAnimationEmitter(b.getInt("anim"),
                                        b.getInt("coordx"), b.getInt("coordy")));
                    }
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
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
        if(sSettings.net_server) {
            String cmdString = "damageplayer " + dmgvictim.get("id") + " " + adjusteddmg + " " + killerid;
            nServer.instance().addNetCmd(cmdString);
        }
    }

    public static int[] getNewPrefabDims() {
        if(cVars.get("newprefabname").contains("room_large")) {
            return new int[]{2400, 2400};
        }
        if(cVars.isVal("newprefabname", "end_wall")) {
            return new int[]{300, 300};
        }
        if(cVars.isVal("newprefabname", "end_cap")) {
            return new int[]{300, 150};
        }
        if(cVars.isVal("newprefabname", "cube")) {
            return new int[]{300, 300};
        }
        if(cVars.isVal("newprefabname", "cube_large")) {
            return new int[]{600, 600};
        }
        return new int[]{1200, 1200};
    }

    public static int[] getPlaceObjCoords() {
        int[] mc = cScripts.getMouseCoordinates();
        int[] fabdims = getNewPrefabDims();
        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0])+cVars.getInt("camx") - fabdims[0]/2,
                cEditorLogic.state.snapToX);
        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1])+cVars.getInt("camy") - fabdims[1]/2,
                cEditorLogic.state.snapToY);
        return new int[]{pfx, pfy};
    }

    public static void setupGame() {
        cVars.putLong("starttime", System.currentTimeMillis());
        if(sSettings.show_mapmaker_ui && uiInterface.inplay) {
            //spawns player for mapmaker testing
            xCon.ex("gounspectate");
        }
        cGameLogic.resetGameState();
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

    public static void changeBotWeapon(gPlayer cl, int newweapon, boolean fromPowerup) {
        HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
        if(botsMap.size() > 0 && !(!fromPowerup && newweapon != 0)) {
            nServer.instance().clientArgsMap.get(cl.get("id")).put("weapon", Integer.toString(newweapon));
            checkPlayerSpriteFlip(cl);
        }
    }

    public static void changeWeapon(int newweapon) {
        gPlayer p = cGameLogic.userPlayer();
        if(p != null) {
            if(newweapon != p.getInt("weapon"))
                xCon.ex("playsound sounds/grenpinpull.wav");
            p.putInt("weapon", newweapon);
            checkPlayerSpriteFlip(cGameLogic.userPlayer());
        }
    }

    public static void goToEndScreen() {
        cVars.putLong("intermissiontime",
                System.currentTimeMillis() + Integer.parseInt(sVars.get("intermissiontime")));
    }

    public static boolean isNetworkGame() {
        return sSettings.net_server || sSettings.net_client;
    }
}
