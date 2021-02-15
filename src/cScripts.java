import java.awt.*;
import java.util.*;

public class cScripts {
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
        if(cVars.getInt("mapview") == gMap.MAP_TOPVIEW
                && (p.getDouble("fv") >= 7*Math.PI/4 && p.getDouble("fv") <= 9*Math.PI/4)) {
            if(!p.get("pathsprite").contains("a00")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a00.png", p.get("color"))));
            }
        }
        else if((cVars.getInt("mapview") == gMap.MAP_TOPVIEW && p.getDouble("fv") <= 3*Math.PI/4)
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
        else if(cVars.getInt("mapview") == gMap.MAP_TOPVIEW && p.getDouble("fv") <= 5*Math.PI/4) {
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
                gWeapons.fromCode(p.getInt("weapon")).dims[1] =
                    gWeapons.fromCode(p.getInt("weapon")).flipdiml;
                gWeapons.fromCode(p.getInt("weapon")).setSpriteFromPath(eUtils.getPath(sprite));
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

    public static synchronized void selectThingUnderMouse(int objType) {
        int[] mc = getMouseCoordinates();
        switch (objType) {
            case gScene.THING_TILE:
                for (int i=eManager.currentMap.scene.tiles().size()-1; i >= 0; i--) {
                    gTile t = eManager.currentMap.scene.tiles().get(i);
                    if(t.coordsWithinBounds(mc[0], mc[1]) && (cEditorLogic.state.selectedTileId != i)) {
                        xCon.ex(String.format("e_selecttile %d", i));
                        return;
                    }
                }
                break;
            case gScene.THING_PROP:
                for (int i=eManager.currentMap.scene.props().size()-1; i >= 0; i--) {
                    gProp t = eManager.currentMap.scene.props().get(i);
                    if(t.coordsWithinBounds(mc[0], mc[1]) && (cEditorLogic.state.selectedPropId != i)) {
                        xCon.ex(String.format("e_selectprop %d", i));
                        return;
                    }
                }
                break;
            case gScene.THING_FLARE:
                for (int i=eManager.currentMap.scene.flares().size()-1; i >= 0; i--) {
                    gFlare t = eManager.currentMap.scene.flares().get(i);
                    if(t.coordsWithinBounds(mc[0], mc[1]) && cEditorLogic.state.selectedFlareTag != i) {
                        xCon.ex(String.format("e_selectflare %d", i));
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void checkPlayerScorepoints(gProp scorepoint, gPlayer pla) {
        HashMap<String, gThing> scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
        //nonlinear race
        if(cVars.getInt("gamemode") == cGameMode.RACE) {
            if(sSettings.net_server && pla.get("id").contains("bot")) {
                if(!scorepoint.get("racebotidcheckins").contains(pla.get("id"))) {
                    scorepoint.put("racebotidcheckins", scorepoint.get("racebotidcheckins")+(pla.get("id")+":"));
                }
                int gonnaWin = 1;
                for(String id : scorepointsMap.keySet()) {
                    gProp p = (gProp) scorepointsMap.get(id);
                    if(!p.get("racebotidcheckins").contains(pla.get("id"))) {
                        gonnaWin = 0;
                        break;
                    }
                }
                if(gonnaWin > 0) {
                    xCon.ex("givepoint "+pla.get("id"));
                    for(String id : scorepointsMap.keySet()) {
                        gProp p = (gProp) scorepointsMap.get(id);
                        p.put("racebotidcheckins",
                                p.get("racebotidcheckins").replace(pla.get("id")+":", ""));
                    }
                }
            }
            else {
                if (scorepoint.isZero("int0")) {
                    scorepoint.putInt("int0", 1);
                    int gonnaWin = 1;
                    for(String id : scorepointsMap.keySet()) {
                        gProp scorepointa = (gProp) scorepointsMap.get(id);
                        if (scorepointa.isZero("int0")) {
                            gonnaWin = 0;
                        }
                    }
                    if (gonnaWin > 0) {
                        for(String id : scorepointsMap.keySet()) {
                            gProp scorepointa = (gProp) scorepointsMap.get(id);
                            scorepointa.put("int0", "0");
                        }
                        if (sSettings.net_server)
                            xCon.ex("givepoint " + pla.get("id"));
                        createScorePopup(pla, 1);
                    }
                }
            }
        }
        // waypoints
        if(cVars.getInt("gamemode") == cGameMode.WAYPOINTS) {
            if(scorepoint.getInt("int0") > 0) {
                scorepoint.put("int0", "0");
                createScorePopup(pla,1);
                if(sSettings.net_server) {
                    xCon.ex("givepoint " + pla.get("id"));
                    cGameMode.checkWaypoints();
                }
            }
        }
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

    public static void refillWeaponStocks() {
        cVars.putInt("weaponstock0", gWeapons.get(gWeapons.type.NONE).maxAmmo);
        cVars.putInt("weaponstock1", gWeapons.get(gWeapons.type.PISTOL).maxAmmo);
        cVars.putInt("weaponstock2", gWeapons.get(gWeapons.type.SHOTGUN).maxAmmo);
        cVars.putInt("weaponstock3", gWeapons.get(gWeapons.type.AUTORIFLE).maxAmmo);
        cVars.putInt("weaponstock4", gWeapons.get(gWeapons.type.LAUNCHER).maxAmmo);
        cVars.putInt("weaponstock5", gWeapons.get(gWeapons.type.GLOVES).maxAmmo);
    }

    public static void clearWeaponStocks() {
        cVars.put("weaponstock0", "0");
        cVars.put("weaponstock1", "0");
        cVars.put("weaponstock2", "0");
        cVars.put("weaponstock3", "0");
        cVars.put("weaponstock4", "0");
        cVars.put("weaponstock5", "0");
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
                            cScripts.createID(8), new gAnimationEmitter(b.getInt("anim"),
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
                                cScripts.createID(8), new gAnimationEmitter(b.getInt("anim"),
                                        b.getInt("coordx"), b.getInt("coordy")));
                    }
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
            for(String playerId : gScene.getPlayerIds()) {
                gPlayer t = gScene.getPlayerById(playerId);
                if(b.doesCollideWithPlayer(t) && !b.get("srcid").equals(playerId)) {
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
            eManager.currentMap.scene.getThingMap("THING_BULLET").remove(
                    bulletsToRemovePlayerMap.get(p).get("id"));
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

    public static void createScorePopup(gPlayer p, int points) {
        eManager.currentMap.scene.getThingMap("THING_POPUP").put(createID(8),
                new gPopup(p.getInt("coordx") + (int)(Math.random()*(p.getInt("dimw")+1)),
                p.getInt("coordy") + (int)(Math.random()*(p.getInt("dimh")+1)),
                        String.format("+%d", points), 0.0));
    }

    public static String createID(int length) {
        StringBuilder id = new StringBuilder();
        String[] vals = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for(int i = 0; i < length; i++) {
            id.append(vals[(int) (Math.random() * 10.0)]);
        }
        return id.toString();
    }

    //call this everytime a bullet intersects a player
    public static void createDamagePopup(gPlayer dmgvictim, gBullet bullet) {
        //get shooter details
        String killerid = bullet.get("srcid");
        gPlayer killerPlayer = gScene.getPlayerById(killerid);
        String killername = killerPlayer.get("name");
        if(!dmgvictim.contains("spawnprotectiontime")) {
            //calculate dmg
            int adjusteddmg = bullet.getInt("dmg") - (int)((double)bullet.getInt("dmg")/2
                    *((Math.abs(System.currentTimeMillis() - bullet.getLong("timestamp")
            )/(double)bullet.getInt("ttl"))));
            //play animations on all clients
            eManager.currentMap.scene.getThingMap("THING_POPUP").put(cScripts.createID(8),
                    new gPopup(dmgvictim.getInt("coordx") + (int)(Math.random()*(dmgvictim.getInt("dimw")+1)),
                            dmgvictim.getInt("coordy") + (int)(Math.random()*(dmgvictim.getInt("dimh")+1)),
                            Integer.toString(adjusteddmg), 0.0));
            if(sVars.isOne("vfxenableanimations") && bullet.getInt("anim") > -1)
                eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                        createID(8), new gAnimationEmitter(gAnimations.ANIM_SPLASH_RED,
                                bullet.getInt("coordx"), bullet.getInt("coordy")));
            //handle damage serverside
            if(sSettings.net_server) {
                String damageplayer_cmdstring = "damageplayer " + dmgvictim.get("id") + " " + adjusteddmg;
                xCon.ex(damageplayer_cmdstring);
                nServer.addSendCmd(damageplayer_cmdstring);
            }
            //serverside actions on player death
            if(dmgvictim.getInt("stockhp") < 1 && !dmgvictim.contains("respawntime")) {
                cScripts.playPlayerDeathSound();
                if (sSettings.net_server) {
                    cScoreboard.incrementScoreFieldById(killerid, "kills");
                    xCon.ex("say " + killername + " killed " + dmgvictim.get("name"));
                    if (cVars.getInt("gamemode") == cGameMode.DEATHMATCH) {
                        xCon.ex("givepoint " + killerid);
                    }
                    if((cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
                            || cVars.isInt("gamemode", cGameMode.FLAG_MASTER))
                            && cVars.isVal("flagmasterid", dmgvictim.get("id"))) {
                        cVars.put("flagmasterid", "");
                    }
                }
                dmgvictim.put("coordx", "-10000");
                dmgvictim.put("coordy", "-10000");
            }
        }
    }

    public static void processUserPlayerHPLoss() {
        gPlayer userPlayer = cGameLogic.userPlayer();
        //shake camera
//                        int dmg = oldstockhp - userPlayer.getInt("stockhp");
//                        if(cGameLogic.isUserPlayer(userPlayer)) {
//                            cVars.putLong("shaketime", System.currentTimeMillis()+cVars.getInt("shaketimemax"));
//                            int shakeintensity = Math.min(cVars.getInt("camshakemax"),
//                                    cVars.getInt("camshakemax")*(int)((double)dmg/(double)userPlayer.getInt("stockhp")));
//                            cVars.addIntVal("camx", cVars.getInt("velocitycam")+shakeintensity);
//                            cVars.addIntVal("camy", cVars.getInt("velocitycam")+shakeintensity);
//                        }
        //handle death
        if(userPlayer.getInt("stockhp") < 1 && !userPlayer.contains("respawntime")) {
            //user player
            if(cGameLogic.isUserPlayer(userPlayer)) {
                xCon.ex("dropweapon");
                cVars.remove("shaketime");
                cVars.putInt("cammode", gCamera.MODE_FREE);
                cVars.put("cammov0", "0");
                cVars.put("cammov1", "0");
                cVars.put("cammov2", "0");
                cVars.put("cammov3", "0");
                userPlayer.putInt("alive", 0);
                userPlayer.putLong("respawntime",
                        System.currentTimeMillis() + cVars.getLong("respawnwaittime"));
                userPlayer.put("stockhp", cVars.get("maxstockhp"));
                userPlayer.put("exploded", "0");
                userPlayer.putInt("explodex", cGameLogic.userPlayer().getInt("coordx") - 75);
                userPlayer.putInt("explodey", cGameLogic.userPlayer().getInt("coordy") - 75);
            }
            //everyone does this
            cScripts.playPlayerDeathSound();
            if(sVars.isOne("vfxenableanimations")) {
                eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                        cScripts.createID(8),
                        new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                userPlayer.getInt("coordx") - 75, userPlayer.getInt("coordy") - 75));
            }
            userPlayer.put("coordx", "-10000");
            userPlayer.put("coordy", "-10000");
        }
    }

    public static int[] getPlaceObjCoords() {
        int[] mc = getMouseCoordinates();
        int w = cEditorLogic.state.newTile.getInt("dimw");
        int h = cEditorLogic.state.newTile.getInt("dimh");
        int px = eUtils.roundToNearest(
            eUtils.unscaleInt(mc[0])+cVars.getInt("camx")-w/2,
            cEditorLogic.state.snapToX);
        int py = eUtils.roundToNearest(
            eUtils.unscaleInt(mc[1])+cVars.getInt("camy")-h/2,
            cEditorLogic.state.snapToY);
        return new int[]{px,py};
    }

    public static boolean allClientsReceivedMessage(String msg) {
        for(String id : nServer.clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid) && !nServer.clientArgsMap.get(id).containsKey(msg)) {
                return false;
            }
        }
        return true;
    }

    public static void moveTileDown(int tag) {
        if(tag > 0) {
            gTile tmp = eManager.currentMap.scene.tiles().get(tag-1);
            eManager.currentMap.scene.tiles().set(tag-1, eManager.currentMap.scene.tiles().get(tag));
            eManager.currentMap.scene.tiles().set(tag, tmp);
        }
    }

    public static void movetileUp(int tag) {
        if(tag < eManager.currentMap.scene.tiles().size()-1) {
            gTile tmp = eManager.currentMap.scene.tiles().get(tag+1);
            eManager.currentMap.scene.tiles().set(tag+1, eManager.currentMap.scene.tiles().get(tag));
            eManager.currentMap.scene.tiles().set(tag, tmp);
        }
    }

    public static void setupGame() {
        int[] npcs = new int[] {-6000,-6000};
        cVars.putLong("starttime", System.currentTimeMillis());
        if(cScripts.isNetworkGame() || uiInterface.inplay) {
            gPlayer player0 = new gPlayer(npcs[0], npcs[1],150,150,
                eUtils.getPath(String.format("animations/player_%s/a03.png", sVars.get("playercolor"))));
            player0.put("tag", "0");
            player0.put("id", uiInterface.uuid);
            cGameLogic.setUserPlayer(player0);
            if(sSettings.net_server) {
                player0.put("id", "server");
            }
            player0.put("color", sVars.get("playercolor"));
            eManager.currentMap.scene.playersMap().put(player0.get("id"), player0);
            xCon.ex("centercamera");
        }
        //network
        if(isNetworkGame()) {
            for(String s : nServer.clientIds) {
                gPlayer player = new gPlayer(-6000, -6000,150,150,
                    eUtils.getPath("animations/player_red/a03.png"));
                player.putInt("tag", eManager.currentMap.scene.playersMap().size());
                player.put("id", s);
                eManager.currentMap.scene.playersMap().put(s, player);
            }
            xCon.ex("respawn");
        }
        else if(uiInterface.inplay){
            xCon.ex("respawn");
        }
        cGameLogic.resetGameState();
        cVars.put("canvoteskip", "");
        cVars.put("voteskipctr", "0");
        if(sSettings.net_server) {
            cVars.put("serveraddbots", "");
            cVars.putLong("serveraddbotstime", System.currentTimeMillis() + 1000);
        }
        for(String s : eManager.currentMap.execLines) {
            xCon.ex(s.replaceFirst("cmd ", ""));
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

    public static void changeBotWeapon(gPlayer cl, int newweapon, boolean fromPowerup) {
        HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
        if(botsMap.size() > 0 && !(!fromPowerup && newweapon != 0)) {
            nServer.clientArgsMap.get(cl.get("id")).put("weapon", Integer.toString(newweapon));
            checkPlayerSpriteFlip(cl);
        }
    }

    public static void changeWeapon(int newweapon) {
        if(eManager.currentMap.scene.playersMap().size() > 0
                && newweapon == 0) {
            cGameLogic.userPlayer().putInt("weapon", newweapon);
            xCon.ex("playsound sounds/grenpinpull.wav");
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
