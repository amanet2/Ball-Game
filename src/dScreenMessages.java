import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.util.HashMap;

public class dScreenMessages {
    static FontRenderContext fontrendercontext =
        new FontRenderContext(null, false, true);

    public static void drawCenteredString(Graphics g, String s, int x, int y) {
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2,y);
    }

    public static void drawRightJustifiedString(Graphics g, String s, int x, int y) {
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth(),y);
    }

    public static void setFontColorByTitle(Graphics g, String fonttitle) {
        g.setColor(new Color(Integer.parseInt(xCon.ex(fonttitle).split(",")[0]),
                Integer.parseInt(xCon.ex(fonttitle).split(",")[1]),
                Integer.parseInt(xCon.ex(fonttitle).split(",")[2]),
                Integer.parseInt(xCon.ex(fonttitle).split(",")[3])));
    }

    public static void drawVirusTagString(Graphics g) {
        if(nServer.clientArgsMap != null && nServer.clientArgsMap.containsKey("server")
                && nServer.clientArgsMap.get("server").containsKey("state")) {
            String statestr = nServer.clientArgsMap.get("server").get("state");
            String[] stoks = statestr.split("-");
            String virusString = ">>VIRUS STRING NOT AVAILABLE<<";
            if(stoks.length > 1) {
                int virusplayers = 0;
                int totalplayers = stoks[1].length();
                for (int i = 0; i < totalplayers; i++) {
                    char c = stoks[1].charAt(i);
                    virusplayers += c == '1' ? 1 : 0;
                }
                virusString = String.format("%d/%d PLAYERS INFECTED",virusplayers,totalplayers);
            }
            dScreenMessages.drawCenteredString(g, virusString.toString(),
                    sSettings.width/2,14*sSettings.height/15);
        }
    }

    public static void displayScreenMessages(Graphics g) {
        setFontColorByTitle(g, "textcolornormal");
        cScripts.setFontSmall(g);
        //scale
        if(sVars.isOne("showscale") && eUtils.zoomLevel != 1.0) {
            g.drawString("ZOOM:" + eUtils.zoomLevel, 0, sSettings.height / 64);
        }
        //ticks
        if(sVars.isOne("showtick")) {
            g.drawString("GAME:" + uiInterface.tickReport, 0, 2*sSettings.height / 64);
        }
        //fps
        if(sVars.isOne("showfps")) {
            g.drawString("FPS:" + uiInterface.fpsReport, 0, 3*sSettings.height / 64);
        }
        //mousexy for editor
        if(sSettings.show_mapmaker_ui) {
            g.drawString(String.format("Mouse: %d,%d",
                cScripts.getPlaceObjCoords()[0],cScripts.getPlaceObjCoords()[1]),0,4*sSettings.height/64);
        }
        //net
        if(sVars.isOne("shownet")) {
            g.drawString("Net:" + uiInterface.netReport, 0, 5 * sSettings.height / 64);
            g.drawString("Ping:" + cScoreboard.scoresMap.get(uiInterface.uuid).get("ping"),
                    0, 6 * sSettings.height / 64);
        }
        if(sSettings.show_mapmaker_ui) {
            //camera
            String camstring = String.format("Cam: %d,%d",
                uiInterface.camReport[0], uiInterface.camReport[1]);
            camstring += (cVars.isInt("cammode", gCamera.MODE_FREE)) && cGameLogic.userPlayer() != null
                    ? " (Press 'E' to re-center)" : "";
            g.drawString(camstring,0, 8 * sSettings.height / 64);
            //instance
            if(cGameLogic.userPlayer() != null) {
                g.drawString(String.format("Player: %d,%d",
                        cGameLogic.userPlayer().getInt("coordx"),
                        cGameLogic.userPlayer().getInt("coordy")),
                        0,9*sSettings.height/64);
            }
            // create tile
            g.drawString("New Thing Type: " + gScene.object_titles[cEditorLogic.state.createObjCode],
                    0, 11 * sSettings.height / 64);
            String thingdims = String.format("[%s,%s]", cEditorLogic.state.newTile.get("dimw"),
                    cEditorLogic.state.newTile.get("dimh"));
            if(cEditorLogic.state.createObjCode == gScene.THING_PROP)
                thingdims = String.format("[%s,%s]", cEditorLogic.state.newProp.get("dimw"),
                        cEditorLogic.state.newProp.get("dimh"));
            if(cEditorLogic.state.createObjCode == gScene.THING_FLARE)
                thingdims = String.format("[%s,%s]", cEditorLogic.state.newFlare.get("dimw"),
                        cEditorLogic.state.newFlare.get("dimh"));
            g.drawString(String.format("New Thing Dims: %s", thingdims), 0, 12 * sSettings.height / 64);
            // selected tile
            g.drawString("Selected Thing Details:",0,14*sSettings.height/64);
            g.drawString("---",0,15*sSettings.height/64);
            if(cEditorLogic.state.createObjCode == gScene.THING_TILE
            && cEditorLogic.state.selectedTileId < eManager.currentMap.scene.tiles().size()) {
                gTile t = eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId);
                int c = 0;
                for(String s : t.vars().keySet()) {
                    g.drawString(
                            String.format("%s: %s", s, t.get(s)),0,(16+c)*sSettings.height/64);
                    c++;
                }
            }
            if(cEditorLogic.state.createObjCode == gScene.THING_PROP
                    && cEditorLogic.state.selectedPropId < eManager.currentMap.scene.props().size()) {
                gProp t = eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId);
                int c = 0;
                for(String s : t.vars().keySet()) {
                    g.drawString(
                            String.format("%s: %s", s, t.get(s)),0,(16+c)*sSettings.height/64);
                    c++;
                }
            }
            if(cEditorLogic.state.createObjCode == gScene.THING_FLARE
                    && cEditorLogic.state.selectedFlareTag< eManager.currentMap.scene.flares().size()) {
                gFlare t = eManager.currentMap.scene.flares().get(cEditorLogic.state.selectedFlareTag);
                int c = 0;
                for (String s : t.vars().keySet()) {
                    g.drawString(
                            String.format("%s: %s", s, t.get(s)), 0, (16 + c) * sSettings.height / 64);
                    c++;
                }
            }
        }
        //ingame messages
        setFontColorByTitle(g, "textcolornormal");
        if(uiInterface.inplay) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(eUtils.scaleInt(10)));
            //camera indicator
            if(cVars.getInt("cammode") == gCamera.MODE_FREE) {
                for(Integer i : xCon.instance().pressBinds.keySet()) {
                    if(xCon.instance().pressBinds.get(i).contains("centercamera")) {
                        dScreenMessages.drawCenteredString(g,"PRESS '"+ KeyEvent.getKeyText(i)+"' TO CENTER CAMERA",
                                sSettings.width / 2, sSettings.height - sSettings.height / 64);
                    }
                }
            }
            //flashlight
            if(cVars.isOne("flashlight")) {
                g.setColor(new Color(0,0,0,255));
                g.fillRect(sSettings.width/64,56*sSettings.height/64,sSettings.width/3,
                        sSettings.height/64);
                g.setColor(new Color(210,160,0,255));
                g.fillRect(sSettings.width/64,56*sSettings.height/64,
                        sSettings.width/3,sSettings.height/64);
                g.setColor(new Color(150,130,0,255));
                g.drawRect(sSettings.width/64,56*sSettings.height/64,sSettings.width/3,
                        sSettings.height/64);
                g.setColor(new Color(200,200,200,255));
                g.drawString("FLASHLIGHT", sSettings.width/62,56*sSettings.height/64);
            }
            //health
            g.setColor(new Color(0,0,0,255));
            g.fillRect(sSettings.width/64,58*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
            g.setColor(new Color(220,0,30,255));
            g.fillRect(sSettings.width/64,58*sSettings.height/64,
                sSettings.width/3*cVars.getInt("stockhp")/cVars.getInt("maxstockhp"),sSettings.height/64);
            g.setColor(new Color(150,0,0,255));
            g.drawRect(sSettings.width/64,58*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
            if(cVars.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                cVars.getLong("hprechargetime") + cVars.getInt("delayhp")
                        >= System.currentTimeMillis()) {
                double reloadratio = (double)(
                        cVars.getLong("hprechargetime") + cVars.getInt("delayhp")
                                - System.currentTimeMillis())/cVars.getInt("delayhp");
                g.setColor(new Color(255,60,150,100));
                g.fillRect(sSettings.width/64,58*sSettings.height/64,
                        (int)(sSettings.width/3*reloadratio),
                        sSettings.height/64);
            }
            g.setColor(new Color(200,200,200,255));
            g.drawString("HEALTH", sSettings.width/62,58*sSettings.height/64);
            //ammo
            g.setColor(new Color(0,0,0,255));
            g.fillRect(sSettings.width/64,60*sSettings.height/64, sSettings.width/3,
                    sSettings.height/64);
            if(cScripts.isReloading()) {
                double reloadratio = (double)(
                        cVars.getLong("weapontime"+cVars.get("currentweapon"))+cVars.getInt("delayweap")
                        - System.currentTimeMillis())/cVars.getInt("delayweap");
                g.setColor(new Color(255,255,255,100));
                g.fillRect(sSettings.width/64,60*sSettings.height/64,
                        (int)(sSettings.width/3-(sSettings.width/3*reloadratio)),
                        sSettings.height/64);
            }
            else {
                g.setColor(new Color(30,50,220,255));
                if(gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo > 0)
                    g.fillRect(sSettings.width/64,60*sSettings.height/64,
                            sSettings.width/3*cVars.getInt("weaponstock"+cVars.get("currentweapon"))
                                    /gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo,
                            sSettings.height/64);
            }
            g2.setColor(Color.BLACK);
            for(int j = 0; j < gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo;j++) {
                g2.drawRect(
                        sSettings.width/64
                                + (j*((sSettings.width/3)/gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo)),
                        60*sSettings.height/64,
                        ((sSettings.width/3)/gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo),
                        sSettings.height/64);
            }
            g.setColor(new Color(0,0,150,255));
            g.drawRect(sSettings.width/64,60*sSettings.height/64,sSettings.width/3,
                    sSettings.height/64);
//            if(cGameLogic.getUserPlayer().getLong("cooldown") >= System.currentTimeMillis()) {
//                double reloadratio = ((double)(cGameLogic.getUserPlayer().getLong("cooldown")
//                    - System.currentTimeMillis())/(gWeapons.weapons_selection[
//                        cGameLogic.getUserPlayer().getInt("weapon")].refiredelay));
//                System.out.println(reloadratio);
//                g.setColor(new Color(0,255,255,100));
//                g.fillRect(sSettings.width/64,60*sSettings.height/64,
//                        (int)(sSettings.width/3*reloadratio),sSettings.height/64);
//            }
            g.setColor(new Color(200,200,200,255));
            g.drawString(cScripts.isReloading() ? "-- RELOADING --"
                    : cVars.getInt("currentweapon") != gWeapons.type.NONE.code()
                    && cVars.getInt("currentweapon") != gWeapons.type.GLOVES.code()
                    ? (gWeapons.fromCode(cVars.getInt("currentweapon")).name.toUpperCase()
                    + " ["+cVars.getInt("weaponstock"+cVars.getInt("currentweapon"))+ "]")
                    : gWeapons.fromCode(cVars.getInt("currentweapon")).name.toUpperCase(),
                    sSettings.width/62,60*sSettings.height/64);
            //sprint
            g.setColor(new Color(0,0,0,255));
            g.fillRect(sSettings.width/64,62*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
            g.setColor(new Color(20,170,80,255));
            g.fillRect(sSettings.width/64,62*sSettings.height/64,
                sSettings.width/3*cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed"),sSettings.height/64);
            g.setColor(new Color(0,100,25,255));
            g.drawRect(sSettings.width/64,62*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
            g.setColor(new Color(200,200,200,255));
            g.drawString("BOOST", sSettings.width/62,62*sSettings.height/64);
        }
        //big font
        cScripts.setFontNormal(g);
        if(uiInterface.inplay) {
            if(cScripts.isNetworkGame()) {
                drawRightJustifiedString(g, String.format("%s", cVars.isOne("gameteam") ? "-- TEAM GAME --" : ""),
                        29 * sSettings.width / 30, sSettings.height - 4 * sSettings.height / 30);
                long timeleft = cVars.getLong("timeleft");
                if(timeleft < 30000) {
                    setFontColorByTitle(g, "textcoloralert");
                }
                drawRightJustifiedString(g, eUtils.getTimeString(cVars.getLong("timeleft")),
                        29 * sSettings.width / 30, sSettings.height - 3 * sSettings.height / 30);
                setFontColorByTitle(g, "textcolorhighlight");
                drawRightJustifiedString(g,
                        cScoreboard.scoresMap.get(cGameLogic.userPlayer().get("id")).get("score") + " points",
                        29 * sSettings.width / 30, sSettings.height - 2 * sSettings.height / 30);
                setFontColorByTitle(g, "textcolornormal");
                drawRightJustifiedString(g, cVars.get("scorelimit") + " points to win | "
                                + cGameMode.net_gamemode_texts[cVars.getInt("gamemode")].toUpperCase(),
                        29 * sSettings.width / 30, sSettings.height - sSettings.height / 30);
            }
        }
        //wip notice
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                100));
        drawCenteredString(g, "WORK IN PROGRESS",
                sSettings.width/2, sSettings.height - sSettings.height / 6);
        //big font
        cScripts.setFontNormal(g);
        //say
        if(gMessages.enteringMessage) {
            String ps = gMessages.enteringOptionText.length() > 0 ? gMessages.enteringOptionText : "SAY";
            g.drawString(String.format("%s: %s",ps,gMessages.msgInProgress),
                0,sSettings.height/2-sSettings.height/64);
        }
        //respawn
        if(cVars.contains("respawntime")) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0, sSettings.width,sSettings.height/4);
            g.fillRect(0,3*sSettings.height/4, sSettings.width,sSettings.height/4);
            setFontColorByTitle(g, "textcoloralert");
            drawCenteredString(g, "RESPAWN IN " +
                            eUtils.getTimeString(cVars.getLong("respawntime") - System.currentTimeMillis()),
                    sSettings.width / 2, sSettings.height/6);
        }
        setFontColorByTitle(g, "textcolorhighlight");
        //sendmsg.. invisible?
        setFontColorByTitle(g, "textcolornormal");
        //menus
        if(!uiInterface.inplay) {
            if(!sSettings.show_mapmaker_ui) {
                if(uiMenus.selectedMenu == uiMenus.MENU_CONTROLS)
                    dMenus.showControlsMenu(g);
                else if(uiMenus.selectedMenu == uiMenus.MENU_CREDITS)
                    dMenus.showCreditsMenu(g);
                else
                    dMenus.showPauseMenu(g);
                if(uiMenus.gobackSelected)
                    setFontColorByTitle(g, "textcolorbonus");
                g.drawString("[Esc] GO BACK",0,15*sSettings.height/16);
            }
            else {
                g.drawString(String.format("{TILES : %d, PROPS: %d, FLARES: %d}",
                    eManager.currentMap.scene.tiles().size(), eManager.currentMap.scene.props().size(),
                    eManager.currentMap.scene.flares().size()),
                        0,15*sSettings.height/16);
                g.drawString(String.format("press [Esc] to test %s", eManager.currentMap.mapName), 0,
                    31*sSettings.height/32);
            }
        }
        //console
        cScripts.setFontConsole(g);
        if(sVars.isOne("inconsole")) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,sSettings.width,sSettings.height);
            g.setColor(new Color(100,100,150, 100));
            g.fillRect(0,0,sSettings.width, (xCon.instance().linesToShow+2)*sSettings.height/64);
            setFontColorByTitle(g, "textcolornormal");
            int ctr = 0;
            for(int i = xCon.instance().linesToShowStart;
                i < xCon.instance().linesToShowStart+ xCon.instance().linesToShow; i++) {
                int dd = 1;
                if(i == xCon.instance().linesToShowStart && xCon.instance().linesToShowStart > 0) {
                    g.drawString(String.format("--- (%d) scroll up ---", xCon.instance().linesToShowStart),
                        0, (ctr + 1) * sSettings.height / 64);
                    dd = 0;
                }
                if(i == xCon.instance().linesToShowStart+ xCon.instance().linesToShow-1
                    && xCon.instance().linesToShowStart
                    < xCon.instance().stringLines.size() - xCon.instance().linesToShow) {
                    g.drawString(String.format("--- (%d) scroll down ---",
                        xCon.instance().stringLines.size()- xCon.instance().linesToShowStart
                            - xCon.instance().linesToShow),
                        0, (ctr + 1) * sSettings.height / 64);
                    dd = 0;
                }
                if(dd != 0){
                    g.drawString(xCon.instance().stringLines.get(i),
                        0, (ctr + 1) * sSettings.height / 64);
                }
                ctr++;
            }
            StringBuilder is = new StringBuilder();
            for(int i = 0; i < xCon.instance().cursorIndex; i++) {
                is.append(" ");
            }
            is = new StringBuilder(System.currentTimeMillis() % 500 > 250 ? is.toString() : String.format("%s_", is.toString()));
            g.drawString(String.format("console:~$ %s", xCon.instance().commandString),
                0,(xCon.instance().linesToShow+1)*sSettings.height/64);
            g.drawString(String.format("           %s", is.toString()), 0,
                (xCon.instance().linesToShow+1)*sSettings.height/64);
        }
        //big font
        cScripts.setFontNormal(g);
        //respawn msg
        //scoreboard
        if(cVars.isOne("showscore")) {
            dScoreboard.showScoreBoard(g);
        }
        else if(eManager.currentMap.scene.players().size() > 0){
//            System.out.println(nServer.clientArgsMap.toString());
            if(nServer.clientArgsMap.get("server") != null
            && nServer.clientArgsMap.get("server").get("topscore") != null
            && nServer.clientArgsMap.get("server").get("topscore").length() > 0) {
                if(cScoreboard.isTopScoreId(cGameLogic.userPlayer().get("id"))) {
                    setFontColorByTitle(g, "textcolorhighlight");
                }
                dScreenMessages.drawCenteredString(g, "Leader: "
                        + nServer.clientArgsMap.get("server").get("topscore"),
                        sSettings.width / 2, sSettings.height / 30);
                setFontColorByTitle(g, "textcolornormal");
            }
        }
        //safezone timer
        if(cVars.getInt("gamemode") == cGameMode.SAFE_ZONES) {
            dScreenMessages.drawCenteredString(g,">>SELF-DESTRUCT IN "+
                    eUtils.getTimeString(cVars.getLong("safezonetime")-System.currentTimeMillis())
                    + "<<",
                    sSettings.width / 2, 5*sSettings.height/8);
        }

        //game alerts
        if(cGameLogic.userPlayer() != null && cVars.getInt("timeleft") > 0 && cVars.get("winnerid").length() < 1) {
            if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                    || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER) &&
                    cVars.isVal("flagmasterid", uiInterface.uuid)) {
                dScreenMessages.drawCenteredString(g,">>YOU HAVE THE FLAG!<<",
                        sSettings.width / 2, 5*sSettings.height/8);
            }
            if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                    || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER) &&
                    !cVars.isVal("flagmasterid", "")
                    && !cVars.isVal("flagmasterid", uiInterface.uuid)) {
                dScreenMessages.drawCenteredString(g,">>FLAG TAKEN!<<",
                        sSettings.width / 2, 5*sSettings.height/8);
            }
//            // king of flags
//            if(cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS) {
//                StringBuilder todraw = new StringBuilder();
//                //this is where we show the checkmarks for kof flag caps
//                HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
//                for(String id : thingMap.keySet()) {
//                    gProp p = (gProp) thingMap.get(id);
//                    gPlayer flagking = cGameLogic.getPlayerById(p.get("str0"));
//                    todraw.append(String.format("[%s]",flagking != null ? flagking.get("name") : "-----"));
//                }
//                dScreenMessages.drawCenteredString(g, todraw.toString(),
//                        sSettings.width/2,14*sSettings.height/15);
//            }
            if(cVars.getInt("gamemode") == cGameMode.VIRUS) {
                drawVirusTagString(g);
            }
            if(cVars.getInt("gamemode") == cGameMode.VIRUS_SINGLE
                    && cScripts.isVirus()) {
                dScreenMessages.drawCenteredString(g,">>YOU ARE INFECTED<<",
                        sSettings.width / 2, 5*sSettings.height/8);
            }
//            if(cVars.getInt("gamemode") == cGameMode.VIRUS_SINGLE && cVars.get("virussingleid").length() > 0
//                    && (!cVars.get("virussingleid").equals(uiInterface.uuid))) {
//                gPlayer p = cGameLogic.getPlayerById(cVars.get("virussingleid"));
//                if(p != null) {
//                    dScreenMessages.drawCenteredString(g,String.format("%s IS INFECTED",p.get("name")),
//                            sSettings.width / 2, 14*sSettings.height/15);
//                }
//            }
            if((cVars.isInt("gamemode", cGameMode.CHOSENONE)
                    || cVars.isInt("gamemode", cGameMode.ANTI_CHOSENONE)) && (cVars.get("chosenoneid").equals(uiInterface.uuid)
            || (nServer.clientArgsMap.containsKey("server")
                    && nServer.clientArgsMap.get("server").containsKey("chosenoneid")
            && nServer.clientArgsMap.get("server").get("chosenoneid").equals(uiInterface.uuid)))) {
                dScreenMessages.drawCenteredString(g,">>YOU ARE THE VICTIM<<",
                        sSettings.width / 2, 5*sSettings.height/8);
            }
        }
        //win lose
        if((cVars.get("winnerid").length() > 0 && nServer.clientArgsMap.containsKey(cVars.get("winnerid")))) {
            if(cVars.isOne("gameteam")) {
                drawCenteredString(g, nServer.clientArgsMap.get(cVars.get("winnerid")).get("color") + " team wins!",
                        sSettings.width / 2, 5*sSettings.height/8);
            }
            else
                drawCenteredString(g, nServer.clientArgsMap.get(cVars.get("winnerid")).get("name") + " wins!",
                    sSettings.width / 2, 5*sSettings.height/8);
        }
        //timeleft
        if(cScripts.isNetworkGame()) {
            if(cVars.getInt("timeleft") <= 0 || cVars.get("winnerid").length() > 0) {
                drawCenteredString(g,
                        "-- changing map --", sSettings.width / 2, 14*sSettings.height/15);
            }
        }
        //messages
        if(gMessages.screenMessages.size() > 0) {
            for(int i = 0; i < gMessages.screenMessages.size(); i++) {
                String s = gMessages.screenMessages.get(i);
                if(!s.contains(":")) {
                    s = "Server: " + s;
                    setFontColorByTitle(g, "textcolorhighlight");
                }
                g.drawString(s,0,23*sSettings.height/32-(gMessages.screenMessages.size()*(sSettings.height/32))
                    +(i*(sSettings.height/32)));
                setFontColorByTitle(g, "textcolornormal");
            }
        }
    }
}
