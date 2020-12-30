import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class cScripts {
    public static void pointPlayerAtMousePointer() {
        gPlayer p = cGameLogic.getPlayerByIndex(0);
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
        if(cVars.getInt("maptype") == gMap.MAP_TOPVIEW
                && (p.getDouble("fv") >= 7*Math.PI/4 && p.getDouble("fv") <= 9*Math.PI/4)) {
            if(!p.get("pathsprite").contains("a00")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a00.png", p.get("color"))));
            }
        }
        else if((cVars.getInt("maptype") == gMap.MAP_TOPVIEW && p.getDouble("fv") <= 3*Math.PI/4)
            || (p.getDouble("fv") >= 2*Math.PI || p.getDouble("fv") <= 3*Math.PI/4)) {
            if(!p.get("pathsprite").contains("a03")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a03.png",p.get("color"))));
                String sprite = p.isInt("weapon", gWeapons.weapon_autorifle) ? "misc/autorifle.png" :
                    p.isInt("weapon",gWeapons.weapon_shotgun) ? "misc/shotgun.png" :
//                        p.isInt("weapon",gWeapons.weapon_none) ? "misc/glove.png" :
                        p.isInt("weapon",gWeapons.weapon_none) ? "" :
                        p.isInt("weapon",gWeapons.weapon_launcher) ? "misc/launcher.png" :
                            "misc/bfg.png";
                gWeapons.weapons_selection[p.getInt("weapon")].dims[1] =
                    gWeapons.weapons_selection[p.getInt("weapon")].flipdimr;
                gWeapons.weapons_selection[p.getInt("weapon")].setSpriteFromPath(eUtils.getPath(sprite));
            }
        }
        else if(cVars.getInt("maptype") == gMap.MAP_TOPVIEW && p.getDouble("fv") <= 5*Math.PI/4) {
            if(!p.get("pathsprite").contains("a04")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a04.png",p.get("color"))));
            }
        }
        else {
            if(!p.get("pathsprite").contains("a05")) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a05.png",p.get("color"))));
                String sprite = p.isInt("weapon", gWeapons.weapon_autorifle) ? "misc/autorifle_flip.png" :
                    p.isInt("weapon", gWeapons.weapon_shotgun) ? "misc/shotgun_flip.png" :
//                    p.isInt("weapon", gWeapons.weapon_none) ? "misc/glove_flip.png" :
                    p.isInt("weapon", gWeapons.weapon_none) ? "" :
                    p.isInt("weapon", gWeapons.weapon_launcher) ? "misc/launcher_flip.png" :
                        "misc/bfg_flip.png";
                gWeapons.weapons_selection[p.getInt("weapon")].dims[1] =
                    gWeapons.weapons_selection[p.getInt("weapon")].flipdiml;
                gWeapons.weapons_selection[p.getInt("weapon")].setSpriteFromPath(eUtils.getPath(sprite));
            }
        }
    }

    public static synchronized void getUIMenuItemUnderMouse() {
        int[] mc = getMouseCoordinates();
        int[] xBounds = new int[]{0, sSettings.width/4};
        int[] yBounds = sVars.getInt("displaymode") > 0
                ? new int[]{14*sSettings.height/16,15*sSettings.height/16}
                : new int[]{15*sSettings.height/16,sSettings.height};
        if((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
            if(!uiMenus.gobackSelected) {
                uiMenus.gobackSelected = true;
                uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
            }
            return;
        }
        else
            uiMenus.gobackSelected = false;
        if(uiMenus.selectedMenu != uiMenus.MENU_CONTROLS) {
            for(int i = 0; i < uiMenus.menuSelection[uiMenus.selectedMenu].items.length; i++) {
                xBounds = new int[]{sSettings.width/2-sSettings.width/8, sSettings.width/2+sSettings.width/8};
                yBounds = new int[]{11*sSettings.height/30+i*sSettings.height/30,
                        11*sSettings.height/30+(i+1)*sSettings.height/30};
                if(sVars.isIntVal("displaymode", oDisplay.displaymode_windowed)){
                    yBounds[0] += 40;
                    yBounds[1] += 40;
                }
                if((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                    if(uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem != i)
                        uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = i;
                    return;
                }
            }
        }
        uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
    }

    public static void setFontNormal(Graphics g) {
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
        g.setFont(new Font(sVars.get("fontnameui"), sVars.getInt("fontmode"),
                sVars.getInt("fontsize") * sSettings.height / sVars.getInt("gamescale")));
    }

    public static void setFontSmall(Graphics g) {
        g.setFont(new Font(sVars.get("fontnameui"), sVars.getInt("fontmode"),
                sVars.getInt("fontsize")*sSettings.height/sVars.getInt("gamescale")/2));
    }

    public static void setFontConsole(Graphics g) {
        g.setFont(new Font(sVars.get("fontnameconsole"), sVars.getInt("fontmode"),
                sVars.getInt("fontsize")*sSettings.height/sVars.getInt("gamescale")/2));
    }

    public static synchronized void selectThingUnderMouse(int objType) {
        int[] mc = getMouseCoordinates();
        if(objType == gScene.THING_TILE) {
            for (int i=eManager.currentMap.scene.tiles().size()-1; i >= 0; i--) {
                gTile t = eManager.currentMap.scene.tiles().get(i);
                if(t.coordsWithinBounds(mc[0], mc[1]) && (cEditorLogic.state.selectedTileId != i)) {
                    xCon.ex(String.format("e_selecttile %d", i));
                    return;
                }
            }
        }
        else if(objType == gScene.THING_PROP) {
            for (int i=eManager.currentMap.scene.props().size()-1; i >= 0; i--) {
                gProp t = eManager.currentMap.scene.props().get(i);
                if(t.coordsWithinBounds(mc[0], mc[1]) && (cEditorLogic.state.selectedPropId != i)) {
                    xCon.ex(String.format("e_selectprop %d", i));
                    return;
                }
            }
        }
        else if(objType == gScene.THING_FLARE) {
            for (int i=eManager.currentMap.scene.flares().size()-1; i >= 0; i--) {
                gFlare t = eManager.currentMap.scene.flares().get(i);
                if(t.coordsWithinBounds(mc[0], mc[1]) && (cEditorLogic.state.selectedFlareId != i)) {
                    xCon.ex(String.format("e_selectflare %d", i));
                    return;
                }
            }
        }
    }

    private static gProp getExitTeleporter(gProp tp) {
        for(gProp p : eManager.currentMap.scene.props()) {
            if(!p.isVal("tag", tp.get("tag")) && p.isInt("code", gProp.TELEPORTER)
                    && p.isVal("int0", tp.get("int0"))) {
                return p;
            }
        }
        return null;
    }

    public static void teleportPlayer(gProp tp, gPlayer p) {
        gProp exit = getExitTeleporter(tp);
        if(p.get("id").contains("bot") && !p.isVal("exitteleportertag", tp.get("tag"))) {
            if(exit != null) {
                p.putInt("coordx", exit.getInt("coordx") + exit.getInt("dimw")/2 - p.getInt("dimw")/2);
                p.putInt("coordy", exit.getInt("coordy") + exit.getInt("dimh")/2 - p.getInt("dimh")/2);
                p.put("exitteleportertag", exit.get("tag"));
            }
        }
        else if(p.isZero("tag") && !cVars.isVal("exitteleportertag", tp.get("tag"))) {
            xCon.ex("playsound sounds/bfg.wav");
            if(exit != null) {
                p.putInt("coordx", exit.getInt("coordx") + exit.getInt("dimw")/2 - p.getInt("dimw")/2);
                p.putInt("coordy", exit.getInt("coordy") + exit.getInt("dimh")/2 - p.getInt("dimh")/2);
                cVars.put("exitteleportertag", exit.get("tag"));
            }
        }
    }

    public static void checkPlayerScorepoints(gProp scorepoint, gPlayer pla) {
        String useint = "int0";
        if(cVars.getInt("gamemode") == cGameMode.RACE) {
            //for race gamemode
            gProp prev = null;
            int gonnaWin = 1;
            if(pla.get("id").contains("bot")) {
                useint = "botint0";
            }
            if (scorepoint.getInt("tag") > 0)
                prev = eManager.currentMap.scene.props().get(scorepoint.getInt("tag") - 1);
            if (scorepoint.isZero("tag") || prev.getInt(useint) > 0) {
                scorepoint.put(useint, "1");

            }
            for (gProp p : eManager.currentMap.scene.props()) {
                if (p.getInt(useint) < 1 && p.isInt("code", gProp.SCOREPOINT)) {
                    gonnaWin = 0;
                    break;
                }
            }
            if (gonnaWin > 0) {
                for (gProp p : eManager.currentMap.scene.props()) {
                    p.put(useint, "0");
                }
                if (sSettings.net_server) {
                    nServer.givePoint(pla.getInt("tag"));
                    xCon.ex("say " + sVars.get("playername") + " completed a lap!");
                } else if (sSettings.net_client) {
                    xCon.ex("cv_lapcomplete 1");
                }
                createScorePopup(pla,1);
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.WAYPOINTS) {
            if(scorepoint.getInt(useint) > 0) {
                scorepoint.put(useint, "0");
                createScorePopup(pla,1);
                if(sSettings.net_server) {
                    nServer.givePoint(pla.getInt("tag"));
                    cGameLogic.refreshWaypoints();
                }
            }
        }

    }

    public static void checkPlayerSafepoints(gProp safepoint) {
        if(safepoint.getInt("int0") > 0) {
            cVars.put("survivesafezone", "1");
        }
    }

    public static void checkPlayerBlueFlags() {
        if(cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                && cVars.isVal("flagmasterid", uiInterface.uuid)) {
            cVars.put("flagmasterid", "");
            createScorePopup(cGameLogic.getPlayerByIndex(0),1);
            if(sSettings.net_server) {
                nServer.givePoint(0);
                xCon.ex("say " + sVars.get("playername") + " captured the flag!");
            }
        }
    }

    public static void checkPlayerPowerups(gProp powerup) {
        if(powerup.getInt("int0") > 0) {
            //do powerup effect
            String[] powerup_selection = new String[]{"pistol", "shotgun", "autorifle", "launcher", "fast"};
//            String[] powerup_selection = new String[]{"pistol", "shotgun", "autorifle", "launcher", "slow", "fast"};
            int r = (int)(Math.random()*(double)powerup_selection.length);
            if(sSettings.net_server) {
                xCon.ex("say "+sVars.get("playername")+" picked up the " + powerup_selection[r] + "!");
            }
            if(r < 4) {
                if (cVars.isZero("gamespawnarmed")) {
                    changeWeapon(r + 1, true);
                }
            }
//            else if(powerup_selection[r].equals("slow") && cVars.isZero("sicknessslow")) {
//                cVars.putInt("velocityplayer", cVars.getInt("velocityplayerbase")/2);
//                xCon.ex("THING_PLAYER.0.sicknessslow 1");
//                xCon.ex("cv_sicknessslow 1");
//            }
            else if(powerup_selection[r].equals("fast") && cVars.isZero("sicknessfast")) {
                cVars.putInt("velocityplayer", cVars.getInt("velocityplayerbase")*2);
                xCon.ex("THING_PLAYER.0.sicknessfast 1");
                xCon.ex("sicknessfast 1");
            }
            powerup.put("int0", "0");
            cGameLogic.getPlayerByIndex(0).putLong("powerupsusetime",
                    System.currentTimeMillis()+sVars.getLong("powerupsusetimemax"));
        }
    }

    public static String getPowerupsString() {
        StringBuilder str = new StringBuilder();
        for(gProp p : eManager.currentMap.scene.props()) {
            if(p.isInt("code", gProp.POWERUP)) {
                str.append(p.get("int0"));
            }
        }
        return str.toString();
    }

    static void processPowerupsString(String powerupString) {
        int ctr = 0;
        for(gProp p : eManager.currentMap.scene.props()) {
            if(p.isInt("code", gProp.POWERUP)) {
                p.put("int0", Character.toString(powerupString.charAt(ctr)));
                ctr++;
            }
        }
    }

    public static void checkPlayerRedFlags(gProp flag) {
        if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
            && cVars.isVal("flagmasterid", "")) {
            cVars.put("flagmasterid", uiInterface.uuid);
            if(sSettings.net_server) {
                xCon.ex("say " + sVars.get("playername") + " has the flag!");
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS && flag.getInt("int0") != 1) {
            gPlayer cl = cGameLogic.getPlayerByIndex(0);
            int pass = 1;
            for(gPlayer p : eManager.currentMap.scene.players()) {
                //make sure no other players still on the flag
                if(!p.get("id").equals(cl.get("id"))
                        && p.willCollideWithPropAtCoords(flag, p.getInt("coordx"), p.getInt("coordy"))) {
                    pass = 0;
                    break;
                }
            }
            if(pass > 0) {
                flag.put("int0", "1");
                flag.put("botint0", "0");
                if(sSettings.net_server) {
                    cVars.putInArray("kofflagcaps", "1", flag.getInt("tag"));
                    if(sSettings.net_server) {
                        nServer.givePoint(0);
                        xCon.ex("say " + sVars.get("playername") + " captured flag#"+flag.getInt("tag"));
                    }
                }
                createScorePopup(cGameLogic.getPlayerByIndex(0),1);
            }
        }
    }

    public static void checkMsgSpecialFunction(String msg) {
        for(String s : eManager.winClipSelection) {
            String[] ttoks = s.split("\\.");
            if(msg.equalsIgnoreCase(ttoks[0])) {
                xCon.ex("playsound sounds/win/" + s);
                break;
            }
        }
        if(sSettings.net_server && msg.strip().length() > 0 && "skip".contains(msg.toLowerCase().strip())) {
            cVars.addIntVal("voteskipctr", 1);
            if(!(cVars.getInt("voteskipctr") < cVars.getInt("voteskiplimit"))) {
                cVars.put("timeleft", "0");
                xCon.ex(String.format("say [VOTE_SKIP] VOTE TARGET REACHED (%s)", cVars.get("voteskiplimit")));
                xCon.ex("say [VOTE_SKIP] CHANGING MAP...");
            }
            else {
                xCon.ex("say " + String.format("[VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
                        cVars.get("voteskipctr"), cVars.get("voteskiplimit")));
            }
        }
//        if(msg.contains("scored a point")
//        && (cVars.getInt("gamemode") != cGameMode.CHOSENONE)) {
//            double d = Math.random();
//            if(d > 0.90)
//                xCon.ex("playsound sounds/bell1.wav");
//            else if(d > 0.75)
//                xCon.ex("playsound sounds/bell3.wav");
//            else
//                xCon.ex("playsound sounds/bell2.wav");
//        }
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
            sVars.putInt("timelimit", (int)(Double.parseDouble(enteredText)*60000.0));
            uiMenus.menuSelection[uiMenus.MENU_NEWGAME].items[3].text =
                String.format("Time Limit [%s]", Double.toString((double)sVars.getInt("timelimit")/60000.0));
        }
    }

    public static void doPlayerCrouch(gPlayer p) {
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
            if (p != null) {
                p.put("dimh", "75");
                p.setSpriteFromPath(p.get("pathsprite"));
                p.put("crouch", "1");
            }
        }
    }

    public static void stopPlayerCrouch(gPlayer p) {
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
            if (p != null) {
                p.put("dimh", "150");
                p.setSpriteFromPath(p.get("pathsprite"));
                p.put("crouch", "0");
            }
        }
    }

    public static void doPause() {
        uiInterface.inplay = !uiInterface.inplay;
        if(uiInterface.inplay) {
            oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
            xCon.ex("playsound sounds/clampdown.wav");
            if(sSettings.show_mapmaker_ui || !isNetworkGame())
                setupGame();
        }
        else {
            xCon.ex("playsound sounds/grenpinpull.wav");
            if(sSettings.show_mapmaker_ui || !isNetworkGame())
                eManager.currentMap.scene.players().clear();
        }
    }

    public static void createGrenadeExplosion(gBullet seed) {
        for (int i = 0; i < 8; i++) {
            gBullet g = new gBullet(seed.getInt("coordx"),seed.getInt("coordy"), 100, 100,
                    eUtils.getPath("objects/misc/fireorange.png"), 0, gWeapons.weapons_selection[gWeapons.weapon_launcher].damage);
            double randomOffset = (Math.random() * ((Math.PI / 8))) - Math.PI / 16;
            g.putDouble("fv", g.getDouble("fv")+(i * (2.0*Math.PI/8.0) - Math.PI / 16 + randomOffset));
            g.putInt("ttl",150);
            g.putInt("tag", seed.getInt("tag"));
            g.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
            eManager.currentMap.scene.bullets().add(g);
        }
    }

    public static void checkProjectileSplashes() {
        ArrayList<gBullet> trc = new ArrayList<>();
        ArrayList<gAnimationEmitter> tra = new ArrayList<>();
        HashMap<gPlayer,gBullet> trv = new HashMap<>();
        gPopup ttr = null;
        ArrayList<gBullet> pseeds = new ArrayList<>();
        for(gBullet b : eManager.currentMap.scene.bullets()) {
            if(System.currentTimeMillis()-b.getLong("timestamp") > b.getInt("ttl")){
                trc.add(b);
                if(sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1)
                    eManager.currentMap.scene.animations().add(
                        new gAnimationEmitter(b.getInt("anim"), b.getInt("coordx"), b.getInt("coordy")));
                //grenade explosion
                if(b.isInt("src", gWeapons.weapon_launcher)) {
                    pseeds.add(b);
                }
                continue;
            }
            for(gTile t : eManager.currentMap.scene.tiles()) {
                if(b.doesCollideWithinTile(t)) {
                    trc.add(b);
                    if(sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1)
                        eManager.currentMap.scene.animations().add(
                            new gAnimationEmitter(b.getInt("anim"), b.getInt("coordx"), b.getInt("coordy")));
                    if(b.isInt("src", gWeapons.weapon_launcher))
                        pseeds.add(b);
                }
            }
            for(gPlayer t : eManager.currentMap.scene.players()) {
                if(b.doesCollideWithPlayer(t)) {
                    trv.put(t,b);
                    if(b.isInt("src", gWeapons.weapon_launcher))
                        pseeds.add(b);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gBullet pseed : pseeds)
                createGrenadeExplosion(pseed);
        }
        for(gBullet b : trc) {
            eManager.currentMap.scene.bullets().remove(b);
        }
        for(gPlayer p : trv.keySet()) {
            playPlayerDeathSound();
            createDamagePopup(p, trv.get(p));
            eManager.currentMap.scene.bullets().remove(trv.get(p));
        }
        for(gPopup g : eManager.currentMap.scene.popups()) {
            if(g.getLong("timestamp") < System.currentTimeMillis()
                    - cVars.getInt("popuplivetime")) {
                ttr = g;
                break;
            }
        }
        if(ttr != null) {
            eManager.currentMap.scene.popups().remove(ttr);
        }
        //remove finished animations
        for(gAnimationEmitter a : eManager.currentMap.scene.animations()) {
            if(a.getInt("frame") > gAnimations.animation_selection[a.getInt("animation")
                    ].frames.length) {
                tra.add(a);
            }
        }
        for(gAnimationEmitter a : tra) {
            eManager.currentMap.scene.animations().remove(a);
        }
    }

    public static void createScorePopup(gPlayer p, int points) {
        eManager.currentMap.scene.popups().add(new gPopup(p.getInt("coordx") + (int)(Math.random()*(p.getInt("dimw")+1)),
                p.getInt("coordy") + (int)(Math.random()*(p.getInt("dimh")+1)), String.format("+%d", points), 0.0));
    }

    public static String createID(int length) {
        StringBuilder id = new StringBuilder();
        String[] vals = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        for(int i = 0; i < length; i++) {
            id.append(vals[(int) (Math.random() * 10.0)]);
        }
        return id.toString();
    }

    public static void createDamagePopup(gPlayer p, gBullet tr) {
        int d = tr.getInt("dmg")
            -(int)((double)tr.getInt("dmg")
            *((Math.abs(System.currentTimeMillis()-tr.getLong("timestamp"))/(double)tr.getInt("ttl"))));
        String s = String.format("%d", d);
        eManager.currentMap.scene.popups().add(new gPopup(p.getInt("coordx") + (int)(Math.random()*(p.getInt("dimw")+1)),
            p.getInt("coordy") + (int)(Math.random()*(p.getInt("dimh")+1)), s, 0.0));
        if(sVars.isOne("vfxenableanimations") && tr.getInt("anim") > -1)
            eManager.currentMap.scene.animations().add(new gAnimationEmitter(gAnimations.ANIM_SPLASH_RED,
                tr.getInt("coordx"), tr.getInt("coordy")));
        if(p.get("id").contains("bot") && !p.contains("spawnprotectiontime")) {
            int adjusteddmg = tr.getInt("dmg") -(int)((double)tr.getInt("dmg")
                    *((Math.abs(System.currentTimeMillis()-tr.getLong("timestamp"))/(double)tr.getInt("ttl"))));
            cGameLogic.damageBotHealth(p, adjusteddmg);
            if(p.getInt("stockhp") < 1) {
                if(!p.contains("respawntime")) {
                    p.putLong("respawntime", System.currentTimeMillis()+cVars.getLong("respawnwaittime"));
                    p.put("stockhp", cVars.get("maxstockhp"));
                    cVars.put("botexploded", "0");
                    cVars.putInt("botexplodex", p.getInt("coordx") - 75);
                    cVars.putInt("botexplodey", p.getInt("coordy") - 75);
                    cVars.put("botkillername", xCon.ex("THING_PLAYER." + tr.get("tag") + ".name"));
                    cVars.put("botkillerid", xCon.ex("THING_PLAYER." + tr.get("tag") + ".id"));
                    if (sSettings.net_server) {
                        nServer.matchKills[tr.getInt("tag")]++;
                        xCon.ex("say " + cVars.get("botkillername") + " killed " + p.get("name"));
                        if (cVars.getInt("gamemode") == cGameMode.DEATHMATCH) {
                            nServer.givePoint(tr.getInt("tag"));
                        }
                        if(cVars.isInt("gamemode", cGameMode.CHOSENONE)
                                && cVars.isVal("chosenoneid", p.get("id"))) {
                            cVars.put("chosenoneid", cVars.get("botkillerid"));
                        }
                        if(cVars.isInt("gamemode", cGameMode.ANTI_CHOSENONE)
                                && cVars.isVal("chosenoneid", p.get("id"))) {
                            nServer.givePoint(tr.getInt("tag"));
                        }
                        if(cVars.isInt("gamemode", cGameMode.ANTI_CHOSENONE)
                                && cVars.isVal("chosenoneid", cVars.get("botkillerid"))) {
                            nServer.givePoint(tr.getInt("tag"));
                            xCon.ex("cv_chosenoneid " + p.get("id"));
                        }
                        if((cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
                                || cVars.isInt("gamemode", cGameMode.FLAG_MASTER))
                                && cVars.isVal("flagmasterid", p.get("id"))) {
                            cVars.put("flagmasterid", "");
                        }
                    }
                    if(sVars.isOne("vfxenableanimations"))
                        eManager.currentMap.scene.animations().add(new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                            p.getInt("coordx") - 75, p.getInt("coordy") - 75));
                    p.put("coordx", "-10000");
                    p.put("coordy", "-10000");
                }
            }
        }
        if(p.isZero("tag") && !cVars.contains("spawnprotectiontime")) {
            int adjusteddmg = tr.getInt("dmg") -(int)((double)tr.getInt("dmg")
                    *((Math.abs(System.currentTimeMillis()-tr.getLong("timestamp"))/(double)tr.getInt("ttl"))));
            cGameLogic.damageHealth(adjusteddmg);
            if(cVars.getInt("stockhp") < 1) {
                if(!cVars.contains("respawntime")) {
                    cVars.remove("shaketime");
                    cVars.putInt("cammode", gCamera.MODE_TRACKING);
                    cVars.putInt("camplayertrackingindex", tr.getInt("tag"));
                    xCon.ex("centercamera");
                    cVars.putLong("respawntime", System.currentTimeMillis()+cVars.getLong("respawnwaittime"));
                    playPlayerDeathSound();
                    cVars.put("stockhp", cVars.get("maxstockhp"));
                    cVars.put("exploded", "0");
                    cVars.putInt("explodex", cGameLogic.getPlayerByIndex(0).getInt("coordx") - 75);
                    cVars.putInt("explodey", cGameLogic.getPlayerByIndex(0).getInt("coordy") - 75);
                    cVars.put("killername", xCon.ex("THING_PLAYER." + tr.get("tag") + ".name"));
                    cVars.put("killerid", xCon.ex("THING_PLAYER." + tr.get("tag") + ".id"));
                    if (sSettings.net_server) {
                        nServer.matchKills[tr.getInt("tag")]++;
                        xCon.ex("say " + cVars.get("killername") + " killed " + sVars.get("playername"));
                        if (cVars.getInt("gamemode") == cGameMode.DEATHMATCH) {
                            nServer.givePoint(tr.getInt("tag"));
                        }
                        if(cVars.isInt("gamemode", cGameMode.CHOSENONE)
                        && cVars.isVal("chosenoneid", "server")) {
                            cVars.put("chosenoneid", cVars.get("killerid"));
                        }
                        if(cVars.isInt("gamemode", cGameMode.ANTI_CHOSENONE)
                                && cVars.isVal("chosenoneid", "server")) {
                            nServer.givePoint(tr.getInt("tag"));
                        }
                        if(cVars.isInt("gamemode", cGameMode.ANTI_CHOSENONE)
                                && cVars.isVal("chosenoneid", cVars.get("killerid"))) {
                            nServer.givePoint(tr.getInt("tag"));
                            xCon.ex("cv_chosenoneid server");
                        }
                        if((cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
                                || cVars.isInt("gamemode", cGameMode.FLAG_MASTER))
                        && cVars.isVal("flagmasterid", "server")) {
                            cVars.put("flagmasterid", "");
                        }
                    }
                    if(sVars.isOne("vfxenableanimations"))
                        eManager.currentMap.scene.animations().add(new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_BLUE,
                            p.getInt("coordx") - 75, p.getInt("coordy") - 75));
                    p.put("coordx", "-10000");
                    p.put("coordy", "-10000");
                }
            }
            else {
                cVars.putLong("shaketime", System.currentTimeMillis()+cVars.getInt("shaketimemax"));
                int shakeintensity = Math.min(cVars.getInt("camshakemax"),
                        cVars.getInt("camshakemax")*(int)((double)adjusteddmg/(double)cVars.getInt("stockhp")));
                cVars.addIntVal("camx", cVars.getInt("velocitycam")+shakeintensity);
                cVars.addIntVal("camy", cVars.getInt("velocitycam")+shakeintensity);
            }
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
            if(!id.equals(uiInterface.uuid)) {
                if(!nServer.clientArgsMap.get(id).containsKey(msg)) {
                    return false;
                }
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
                eUtils.getPath(String.format("animations/player_%s/a03.png",
                        xCon.ex("playercolor"))));
            player0.put("tag", "0");
            if(sSettings.net_server)
                player0.put("id", "server");
            xCon.ex("THING_PLAYER.0.color playercolor");
            eManager.currentMap.scene.players().add(player0);
            xCon.ex("centercamera");
        }
        //network
        if(isNetworkGame()) {
            for(String s : nServer.clientIds) {
                gPlayer player = new gPlayer(-6000, -6000,150,150,
                    eUtils.getPath("animations/player_red/a03.png"));
                player.putInt("tag", eManager.currentMap.scene.players().size());
                player.put("id", s);
                eManager.currentMap.scene.players().add(player);
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

    public static HashMap<String,String> getMapFromNetString(String argload) {
        HashMap<String,String> toReturn = new HashMap<>();
        String argstr = argload.substring(1,argload.length()-1);
        for(String pair : argstr.split(",")) {
            String[] vals = pair.split("=");
            toReturn.put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
        }
        return  toReturn;
    }

    public static boolean canSpawnPlayer() {
        for(gTile t : eManager.currentMap.scene.tiles()) {
            if(t.isOne("canspawn") && !cGameLogic.getPlayerByIndex(0).willCollideWithinTileAtCoords(t,
                t.getInt("coordx") + t.getInt("dimw")/2 - cGameLogic.getPlayerByIndex(0).getInt("dimw")/2,
                t.getInt("coordy") + t.getInt("dimh")/2 - cGameLogic.getPlayerByIndex(0).getInt("dimh")/2)) {
                boolean pass = true;
                for(gTile td : eManager.currentMap.scene.tiles()) {
                    if(cGameLogic.getPlayerByIndex(0).willCollideWithinTileAtCoords(td,
                        t.getInt("coordx") + t.getInt("dimw")/2
                            - cGameLogic.getPlayerByIndex(0).getInt("dimw")/2,
                        t.getInt("coordy") + t.getInt("dimh")/2
                            - cGameLogic.getPlayerByIndex(0).getInt("dimh")/2)) {
                        pass = false;
                        break;
                    }
                    if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
                        for (gPlayer target : eManager.currentMap.scene.players()) {
                            if (target.getInt("tag") != cGameLogic.getPlayerByIndex(0).getInt("tag") &&
                                cGameLogic.getPlayerByIndex(0).willCollideWithPlayerAtCoords(target, t.getInt("coordx"),
                                    t.getInt("coordy"))) {
                                pass = false;
                                break;
                            }
                        }
                    }
                }
                if(pass)
                    return true;
            }
        }
        return false;
    }

    public static void playPlayerDeathSound() {
        double r = Math.random();
        if(r > .99)
            xCon.ex("playsound sounds/growl.wav");
        else if(r > .5)
            xCon.ex("playsound sounds/shout.wav");
        else
            xCon.ex("playsound sounds/death.wav");
    }

    public static void changeWeapon(int newweapon, boolean fromPowerup) {
        if(eManager.currentMap.scene.players().size() > 0 && !(!fromPowerup && newweapon != 0
                && cVars.isZero("gamespawnarmed"))) {
            xCon.ex("THING_PLAYER.0.weapon " + newweapon);
            cVars.putInt("currentweapon", newweapon);
            xCon.ex("playsound sounds/grenpinpull.wav");
            checkPlayerSpriteFlip(cGameLogic.getPlayerByIndex(0));
        }
    }

    public static void changeBotWeapon(gPlayer cl, int newweapon, boolean fromPowerup) {
        if(eManager.currentMap.scene.botplayers().size() > 0 && !(!fromPowerup && newweapon != 0
                && cVars.isZero("gamespawnarmed"))) {
            cl.putInt("weapon", newweapon);
            checkPlayerSpriteFlip(cl);
        }
    }

    public static void changeWeapon(int newweapon) {
        if(eManager.currentMap.scene.players().size() > 0
                && !(newweapon != 0 && cVars.isZero("gamespawnarmed"))) {
            xCon.ex("THING_PLAYER.0.weapon " + newweapon);
            cVars.putInt("currentweapon", newweapon);
            xCon.ex("playsound sounds/grenpinpull.wav");
            checkPlayerSpriteFlip(cGameLogic.getPlayerByIndex(0));
        }
    }

    public static boolean isTopScore() {
        for(int i : nServer.scores) {
            if(i > nServer.scores[nClient.clientIndex])
                return false;
        }
        return nServer.scores[nClient.clientIndex] > 0;
    }

    public static void goToEndScreen() {
        cVars.putLong("intermissiontime",
                System.currentTimeMillis() + Integer.parseInt(sVars.get("intermissiontime")));
        if(sSettings.net_server) {
            nServer.instance().setMapvoteSelection();
            xCon.instance().log("VOTE: "+Arrays.toString(nServer.mapvoteSelection));
        }
    }

    public static boolean isNetworkGame() {
        return sSettings.net_server || sSettings.net_client;
    }

    public static String getGameState() {
        if(cVars.getInt("gamemode") == cGameMode.SAFE_ZONES) {
            for(gProp p : eManager.currentMap.scene.props()) {
                if(p.isInt("code", gProp.SAFEPOINT) && p.isInt("int0", 1))
                    return String.format("safezone-%s-%s-", p.get("tag"), cVars.get("safezonetime"));
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.WAYPOINTS) {
            int c = 0;
            for(gProp p : eManager.currentMap.scene.props()) {
                if(p.isInt("code", gProp.SCOREPOINT) && p.getInt("int0") > 0)
                    break;
                c++;
            }
            return String.format("waypoints-%d-", c);
        }
        if(cVars.getInt("gamemode") == cGameMode.BOUNCYBALL) {
            int c = 0;
            for(gProp p : eManager.currentMap.scene.props()) {
                if(p.isInt("code", gProp.SCOREPOINT) && p.getInt("int0") > 0)
                    break;
                c++;
            }
            return String.format("bouncyball-%d-", c);
        }
        // KOF
        if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS) {
            StringBuilder s = new StringBuilder();
            for (int i : cVars.getIntArray("kofflagcaps")) {
                s.append(i);
            }
            return String.format("kingofflags%s", s);
        }
        // VIRUS
        if(cVars.getInt("gamemode") == cGameMode.VIRUS) {
            //check
            if(!cVars.contains("virustags"))
                cGameLogic.refreshVirusPlayers();
            String[] vts = cVars.getArray("virustags");
            //check if reset time
            if(cVars.contains("virusresettime") && cVars.getLong("virusresettime") < System.currentTimeMillis()) {
                cGameLogic.refreshVirusPlayers();
                cVars.remove("virusresettime");
            }
            //check if more players
            if(vts.length != eManager.currentMap.scene.players().size()) {
                String[] na = Arrays.copyOf(vts, eManager.currentMap.scene.players().size());
                for(int i = vts.length; i < na.length; i++) {
                    na[i] = "1";
                }
                cVars.putArray("virustags", na);
            }
            String virusIdsStr = getVirusIdsString();
            //check intersections
            for(int i = 0; i < eManager.currentMap.scene.players().size(); i++) {
                for(int j = 0; j < eManager.currentMap.scene.players().size(); j++) {
                    if(i != j) {
                        gPlayer p = cGameLogic.getPlayerByIndex(i);
                        gPlayer pp = cGameLogic.getPlayerByIndex(j);
                        Rectangle r = new Rectangle(p.getInt("coordx"), p.getInt("coordy"),
                                p.getInt("dimw"), p.getInt("dimh"));
                        Rectangle rr = new Rectangle(pp.getInt("coordx"), pp.getInt("coordy"),
                                pp.getInt("dimw"), pp.getInt("dimh"));
                        if (r.intersects(rr) && (
                                (p.getInt("tag") == 0 && virusIdsStr.contains("server"))
                                || (p.get("id").length() > 0 && virusIdsStr.contains(p.get("id")))
                                || (pp.get("id").length() > 0 && virusIdsStr.contains(pp.get("id")))
                        )) {
                            String[] cvts = cVars.getArray("virustags");
                            if(!cvts[i].equals("1")) {
                                xCon.ex("say " + pp.get("name") + " infected " + p.get("name") + "!");
                            }
                            if(!cvts[j].equals("1")) {
                                xCon.ex("say " + p.get("name") + " infected " + pp.get("name") + "!");
                            }
                            cVars.putInArray("virustags", "1", i);
                            cVars.putInArray("virustags", "1", j);
                        }
                    }
                }
            }
            virusIdsStr = getVirusIdsString();
            //send codes before ids
            StringBuilder virusTagsStr = new StringBuilder();
            for(String s : cVars.getArray("virustags")) {
                virusTagsStr.append(s);
            }
            //check if thing if full, begin countdown to reset if it is
            String[] cts = virusIdsStr.split("-");
            if(cts.length-1 == vts.length && !cVars.contains("virusresettime")) {
                cVars.putLong("virusresettime", System.currentTimeMillis()+cVars.getInt("virusresetwaittime"));
            }
            return String.format("virus-%s%s", virusTagsStr.toString(), virusIdsStr);
        }
        return "";
    }

    private static String getVirusIdsString() {
        StringBuilder virusSb = new StringBuilder();
        for(int i = 0; i < cVars.getArray("virustags").length; i++) {
            if(cVars.getArray("virustags")[i].equals("1")) {
                virusSb.append("-").append(i == 0 ? "server" : cGameLogic.getPlayerByIndex(i).get("id"));
            }
        }
        return virusSb.toString();
    }

    public static void removeNetClient(String id) {
        if(nSend.focus_id.equals(id)){
            nSend.focus_id = "";
        }
        nServer.clientsConnected -=1;
        nServer.clientArgsMap.remove(id);
        int quitterIndex = nServer.clientIds.indexOf(id);
        gPlayer quittingPlayer = eManager.currentMap.scene.players().get(quitterIndex+1);
        eManager.currentMap.scene.players().remove(quitterIndex+1);
        String quitterName = nServer.clientNames.get(quitterIndex);
        nServer.clientIds.remove(id);
        nServer.clientNames.remove(quitterIndex);
        //update wins
        int[] newWins = new int[nServer.clientsConnected+1];
        int c = 0;
        for(int i = 0; i < nServer.matchWins.length; i++) {
            if(i != quitterIndex+1) {
                newWins[c] = nServer.matchWins[i];
                c++;
            }
        }
        nServer.matchWins = newWins;
        //update scores
        int[] newScores = new int[nServer.clientsConnected+1];
        c = 0;
        for(int i = 0; i < nServer.scores.length; i++) {
            if(i != quitterIndex+1) {
                newScores[c] = nServer.scores[i];
                c++;
            }
        }
        nServer.scores = newScores;
        //update kills
        int[] newKills = new int[nServer.clientsConnected+1];
        c = 0;
        for(int i = 0; i < nServer.matchKills.length; i++) {
            if(i != quitterIndex+1) {
                newKills[c] = nServer.matchKills[i];
                c++;
            }
        }
        nServer.matchKills = newKills;
        //update pings
        int[] newPings = new int[nServer.clientsConnected+1];
        c = 0;
        for(int i = 0; i < nServer.matchPings.length; i++) {
            if(i != quitterIndex+1) {
                newPings[c] = nServer.matchPings[i];
                c++;
            }
        }
        nServer.matchPings = newPings;
        if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
            && cVars.isVal("flagmasterid", quittingPlayer.get("id"))) {
            cVars.put("flagmasterid", "");
        }
        xCon.ex(String.format("say %s left the game", quitterName));
    }

    public static String getScoreString() {
        StringBuilder scoreString = new StringBuilder();
        for(int i = 0; i < nServer.scores.length; i++) {
            scoreString.append(String.format("%d-%d-%d-%d:", nServer.matchWins[i], nServer.scores[i],
                    nServer.matchKills[i], nServer.matchPings[i]));
        }
        return scoreString.toString();
    }

    public static String getWinnerId() {
        int highestScore = 0;
        String highestId = "";
        boolean pass = false;
        while (!pass) {
            pass = true;
            for(int j = 0; j < nServer.clientIds.size(); j++) {
                String id = nServer.clientIds.get(j);
                if(nServer.scores[j+1] > highestScore) {
                    pass = false;
                    highestId = id;
                    highestScore = nServer.scores[j+1];
                }
            }
        }
        return highestId;
    }

    public static int getWinnerScore() {
        int highestScore = 0;
        boolean pass = false;
        while (!pass) {
            pass = true;
            for(int j = 0; j < nServer.scores.length; j++) {
                if(nServer.scores[j] > highestScore) {
                    pass = false;
                    highestScore = nServer.scores[j];
                }
            }
        }
        return highestScore;
    }

    public static String getTopScoreString() {
        int topscore = 0;
        String winnerName = "";
        if(cVars.isZero("gameteam")) {
            for(int i = 0; i < nServer.scores.length; i++) {
                if(nServer.scores[i] > topscore) {
                    topscore = nServer.scores[i];
                    if(i == 0) {
                        winnerName = sVars.get("playername") + " (" + nServer.scores[i]+")";
                    }
                    else
                        winnerName = nServer.clientNames.get(i-1) + " (" + nServer.scores[i]+")";
                }
            }
        }
        else {
            String[] colors = sVars.getArray("colorselection");
            int[] colorscores = new int[colors.length];
            Arrays.fill(colorscores, 0);
            for(int i = 0; i < nServer.scores.length; i++) {
                gPlayer p = cGameLogic.getPlayerByIndex(i);
                for(int j = 0; j < colors.length; j++) {
                    if(p.get("color").equals(colors[j])) {
                        colorscores[j] = nServer.scores[i];
                    }
                }
            }
            for(int i = 0; i < colorscores.length; i++) {
                if(colorscores[i] > topscore) {
                    topscore = colorscores[i];
                    winnerName = colors[i] + " (" + colorscores[i]+")";
                }
            }
        }
        return winnerName;
    }
}
