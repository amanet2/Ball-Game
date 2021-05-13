import java.awt.*;

public class dScreenMessages {

    public static void displayScreenMessages(Graphics g) {
        dFonts.setFontSmall(g);
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
                uiInterface.getPlaceObjCoords()[0],uiInterface.getPlaceObjCoords()[1]),0,9*sSettings.height/64);
        }
        //net
        if(sVars.isOne("shownet")) {
            g.drawString("Net:" + uiInterface.netReport, 0, 5 * sSettings.height / 64);
            if(gScoreboard.scoresMap.containsKey(uiInterface.uuid)
                    && gScoreboard.scoresMap.get(uiInterface.uuid).containsKey("ping"))
                g.drawString("Ping:" + gScoreboard.scoresMap.get(uiInterface.uuid).get("ping"),
                        0, 6 * sSettings.height / 64);
        }
        if(sSettings.show_mapmaker_ui) {
            //camera
            String camstring = String.format("Cam: %d,%d",
                uiInterface.camReport[0], uiInterface.camReport[1]);
            g.drawString(camstring,0, 8 * sSettings.height / 64);
            //instance
            if(cClientLogic.getUserPlayer() != null) {
                g.drawString(String.format("Player: %d,%d",
                        cClientLogic.getUserPlayer().getInt("coordx"),
                        cClientLogic.getUserPlayer().getInt("coordy")),
                        0,10*sSettings.height/64);
            }
        }
        //ingame messages
        dFonts.setFontColorNormal(g);
        if(uiInterface.inplay) {
            dHUD.drawHUD(g);
        }
        //big font
        dFonts.setFontNormal(g);
        if(uiInterface.inplay) {
            gPlayer userPlayer = cClientLogic.getUserPlayer();
            long timeleft = cVars.getLong("timeleft");
            if(timeleft > -1) {
                if(timeleft < 30000) {
                    dFonts.setFontColorAlert(g);
                }
                dFonts.drawRightJustifiedString(g, eUtils.getTimeString(timeleft),
                        29 * sSettings.width / 30, sSettings.height - 3 * sSettings.height / 30);
            }
//            else if(cVars.contains("scorelimit") && cVars.getInt("scorelimit") > 0)
//                dFonts.drawRightJustifiedString(g, cVars.get("scorelimit") + " to win",
//                    29 * sSettings.width / 30, sSettings.height - 3 * sSettings.height / 30);
            dFonts.setFontColorHighlight(g);
            if(userPlayer != null && gScoreboard.scoresMap.containsKey(userPlayer.get("id"))) {
                dFonts.drawRightJustifiedString(g,
                        gScoreboard.scoresMap.get(userPlayer.get("id")).get("score") + " points",
                        29 * sSettings.width / 30, sSettings.height - 2 * sSettings.height / 30);
            }
            dFonts.setFontColorNormal(g);
            dFonts.drawRightJustifiedString(g,
                    cGameLogic.net_gamemode_texts[cVars.getInt("gamemode")].toUpperCase(),
                29 * sSettings.width / 30, sSettings.height - sSettings.height / 30);
        }
        //wip notice -> needs to be transparent
        dFonts.setFontColorByTitleWithTransparancy(g,"fontcolornormal", 100);
        dFonts.drawCenteredString(g, "WORK IN PROGRESS",
                sSettings.width/2, 5 * sSettings.height / 6);
        //big font
        dFonts.setFontNormal(g);
        //say
        if(gMessages.enteringMessage) {
            g.drawString(String.format("SAY: %s",gMessages.msgInProgress),
                0,31 * sSettings.height/64);
        }
        //sendmsg.. invisible?
        dFonts.setFontColorNormal(g);
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
                    dFonts.setFontColorBonus(g);
                g.drawString("[Esc] GO BACK",0,15*sSettings.height/16);
            }
            else {
                dFonts.setFontNormal(g);
                String newThingString = cVars.get("newprefabname");
                if(cVars.get("newitemname").length() > 0)
                    newThingString = cVars.get("newitemname");
                String selectedThingString = cVars.get("selectedprefabname");
                if(cVars.get("selecteditemname").length() > 0)
                    selectedThingString = cVars.get("selecteditemname");
                if(cVars.get("selectedprefabid").length() > 0 || cVars.get("selecteditemid").length() > 0)
                    g.drawString("[BACKSPACE] - DELETE " + selectedThingString,0,25*sSettings.height/32);
                g.drawString("[WASD] - MOVE CAMERA",0,27*sSettings.height/32);
                g.drawString(String.format("press [MOUSE LEFT] to place %s", newThingString), 0,
                        29*sSettings.height/32);
                g.drawString(String.format("press [Esc] to test %s", cVars.get("mapname")), 0,
                    31*sSettings.height/32);
            }
        }
        //console
        dFonts.setFontConsole(g);
        if(sVars.isOne("inconsole")) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,sSettings.width,sSettings.height);
            g.setColor(new Color(100,100,150, 100));
            g.fillRect(0,0,sSettings.width, (xCon.instance().linesToShow + 2) * sSettings.height/64);
            dFonts.setFontColorNormal(g);
            int ctr = 0;
            for(int i = xCon.instance().linesToShowStart;
                i < xCon.instance().linesToShowStart + xCon.instance().linesToShow; i++) {
                int dd = 1;
                if(i == xCon.instance().linesToShowStart && xCon.instance().linesToShowStart > 0) {
                    g.drawString(String.format("--- (%d) scroll up ---", xCon.instance().linesToShowStart),
                        0, (ctr + 1) * sSettings.height / 64);
                    dd = 0;
                }
                if(i == xCon.instance().linesToShowStart + xCon.instance().linesToShow-1
                    && xCon.instance().linesToShowStart
                    < xCon.instance().stringLines.size() - xCon.instance().linesToShow) {
                    g.drawString(String.format("--- (%d) scroll down ---",
                        xCon.instance().stringLines.size()- xCon.instance().linesToShowStart
                            - xCon.instance().linesToShow),
                        0, (ctr + 1) * sSettings.height / 64);
                    dd = 0;
                }
                if(dd != 0) {
                    if(xCon.instance().stringLines.size() > i)
                        g.drawString(xCon.instance().stringLines.get(i), 0, (ctr + 1) * sSettings.height / 64);
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
        dFonts.setFontNormal(g);
        //respawn msg
        //scoreboard
        if(cVars.isOne("showscore")) {
            dScoreboard.showScoreBoard(g);
        }
//        else if(nClient.instance().serverArgsMap.get("server") != null
//                && nClient.instance().serverArgsMap.get("server").get("topscore") != null
//                && nClient.instance().serverArgsMap.get("server").get("topscore").length() > 0) {
//                if(cClientLogic.getUserPlayer() != null
//                        && gScoreboard.isTopScoreId(cClientLogic.getUserPlayer().get("id")))
//                    dFonts.setFontColorHighlight(g);
//                dFonts.drawCenteredString(g, "Leader: "
//                                + nClient.instance().serverArgsMap.get("server").get("topscore"),
//                        sSettings.width / 2, sSettings.height / 30);
//                dFonts.setFontColorNormal(g);
//        }

        //game alerts
//        if(cClientLogic.userPlayer() != null && cVars.getInt("timeleft") > 0 && cVars.get("winnerid").length() < 1) {
//            switch(cVars.getInt("gamemode")) {
//                case cGameLogic.VIRUS:
//                    if(nServer.instance().clientArgsMap.containsKey("server")
//                            && nServer.instance().clientArgsMap.get("server").containsKey("state")
//                            && nServer.instance().clientArgsMap.get("server").get("state").contains(
//                                cClientLogic.userPlayer().get("id"))) {
//                        dFonts.drawCenteredString(g,">>YOU ARE INFECTED<<",
//                                sSettings.width / 2, 5*sSettings.height/8);
//                    }
//                    break;
//                case cGameLogic.FLAG_MASTER:
//                    if(nServer.instance().clientArgsMap.get("server").get("state").equals(uiInterface.uuid)) {
//                        dFonts.drawCenteredString(g,">>YOU HAVE THE FLAG!<<",
//                                sSettings.width / 2, 5*sSettings.height/8);
//                    }
//                    else if(nServer.instance().clientArgsMap.get("server").get("state").length() > 0){
//                        dFonts.drawCenteredString(g,">>FLAG TAKEN!<<",
//                                sSettings.width / 2, 5*sSettings.height/8);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
        //win lose
        if((cVars.get("winnerid").length() > 0 && nClient.instance().serverArgsMap.containsKey(cVars.get("winnerid")))) {
            dFonts.drawCenteredString(g, nClient.instance().serverArgsMap.get(cVars.get("winnerid")).get("name") + " wins!",
                    sSettings.width / 2, 5*sSettings.height/8);
        }
        //loading
        if(sSettings.IS_CLIENT && cVars.isZero("maploaded"))
                dFonts.drawCenteredString(g, "-- LOADING --", sSettings.width / 2, 9*sSettings.height/12);
        //timeleft
        if((sVars.getInt("timelimit") > -1 && cVars.getInt("timeleft") < 1)
                || cVars.get("winnerid").length() > 0) {
            dFonts.drawCenteredString(g, "-- MATCH OVER --", sSettings.width / 2, 9*sSettings.height/12);
        }
        //echo messages
        if(gMessages.screenMessages.size() > 0) {
            for(int i = 0; i < gMessages.screenMessages.size(); i++) {
                String s = gMessages.screenMessages.get(i);
                g.setColor(Color.BLACK);
                g.drawString(s,3,23*sSettings.height/32-(gMessages.screenMessages.size()*(sSettings.height/32))
                        +(i*(sSettings.height/32))+3);
                dFonts.setFontColorNormal(g);
                g.drawString(s,0,23*sSettings.height/32-(gMessages.screenMessages.size()*(sSettings.height/32))
                        +(i*(sSettings.height/32)));
            }
        }
    }
}
