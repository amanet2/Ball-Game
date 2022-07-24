import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class dScreenMessages {
    static boolean showscore = false;
    static boolean showfps = false;
    static boolean showcam = false;
    static boolean showmouse = false;
    static boolean shownet = false;
    static boolean showplayer = false;
    static boolean showtick = false;
    static boolean showscale = false;

    public static void displayScreenMessages(Graphics g, long gameTimeMillis) {
        dFonts.setFontSmall(g);
        //scale
        if(showscale)
            g.drawString("ZOOM:" + eUtils.zoomLevel, 0, sSettings.height / 64);
        //ticks
        if(showtick)
            g.drawString("GAME:" + uiInterface.tickReport, 0, 3*sSettings.height / 64);
        //fps
        if(showfps)
            g.drawString("FPS:" + uiInterface.fpsReport, 0, 4*sSettings.height / 64);
        //net
        if(shownet) {
            g.drawString("NET_CL:" + uiInterface.netReportClient, 0, 5 * sSettings.height / 64);
            g.drawString("NET_SV:" + uiInterface.netReportServer, 0, 6 * sSettings.height / 64);
//            if(gScoreboard.scoresMap.containsKey(uiInterface.uuid)
//                    && gScoreboard.scoresMap.get(uiInterface.uuid).containsKey("ping"))
//                g.drawString("Ping:" + gScoreboard.scoresMap.get(uiInterface.uuid).get("ping"),
//                        0, 6 * sSettings.height / 64);
        }
        if(showcam) {
            //camera
            String camstring = String.format("Cam: %d,%d",
                    uiInterface.camReport[0], uiInterface.camReport[1]);
            g.drawString(camstring,0, 8 * sSettings.height / 64);
        }
        if(showmouse) {
            int[] mc = uiInterface.getMouseCoordinates();
            if(sSettings.show_mapmaker_ui)
                g.drawString(String.format("Mouse: %d,%d", uiInterface.getPlaceObjCoords()[0],
                        uiInterface.getPlaceObjCoords()[1]),0,9*sSettings.height/64);
            else
                g.drawString(String.format("Mouse: %d,%d",eUtils.unscaleInt(mc[0]) + gCamera.getX(),
                        eUtils.unscaleInt(mc[1]) + gCamera.getY()),0,9*sSettings.height/64);
        }
        if(showplayer && cClientLogic.getUserPlayer() != null) {
            g.drawString(String.format("Player: %d,%d",
                    cClientLogic.getUserPlayer().getInt("coordx"),
                    cClientLogic.getUserPlayer().getInt("coordy")),
                    0,10*sSettings.height/64);
        }
        //ingame messages
        dFonts.setFontColorNormal(g);
        if(uiInterface.inplay) {
            dHUD.drawHUD(g);
        }
        //big font
        dFonts.setFontNormal(g);
        if(uiInterface.inplay && cClientLogic.maploaded) {
            long timeleft = cClientLogic.timeleft;
            if(timeleft > -1) {
                if(timeleft < 30000) {
                    dFonts.setFontColorAlert(g);
                }
                dFonts.drawRightJustifiedString(g, eUtils.getTimeString(timeleft),
                        29 * sSettings.width / 30, 59*sSettings.height/64);
            }
            dFonts.setFontColorNormal(g);
            dFonts.drawRightJustifiedString(g,
                    cGameLogic.net_gamemode_texts[cClientLogic.gamemode].toUpperCase(),
                29 * sSettings.width / 30, 31*sSettings.height/32);
        }
        //wip notice -> needs to be transparent
        dFonts.setFontColorNormalTransparent(g);
        dFonts.drawCenteredString(g, "WORK IN PROGRESS",
                sSettings.width/2, 31*sSettings.height/32);
        //big font
        dFonts.setFontNormal(g);
        //say
        if(gMessages.enteringMessage) {
            g.drawString(String.format("SAY: %s",gMessages.msgInProgress),0,25 * sSettings.height/32);
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
                g.drawString("[Esc] GO BACK",0,31*sSettings.height/32);
            }
            else if(cClientLogic.maploaded){
                dFonts.setFontNormal(g);
                String newThingString = cClientLogic.newprefabname;
                //preview
                g.setColor(Color.BLACK);
                g.fillRoundRect(4*sSettings.width/5,20*sSettings.height/32,
                        7*sSettings.height/20, 11*sSettings.height/32,
                        sSettings.height/36, sSettings.height/36);
                g.setColor(Color.white);
                g.drawRoundRect(4*sSettings.width/5,20*sSettings.height/32,
                        7*sSettings.height/20, 11*sSettings.height/32,
                        sSettings.height/36, sSettings.height/36);
                dFonts.setFontNormal(g);
//                g.drawString("Preview", 4*sSettings.width/5,31*sSettings.height/32);
                if(uiEditorMenus.newitemname.length() > 0)
                    newThingString = uiEditorMenus.newitemname;
                boolean drawnRotate = false;
                String[] rotates = {"_000", "_090", "_180", "_270"};
                ArrayList<String> rotatesList = new ArrayList<>(Arrays.asList(rotates));
                for(String s : rotatesList) {
                    if(cClientLogic.newprefabname.contains(s)) {
                        g.drawString(String.format("[R] - ROTATE %s",
                                uiEditorMenus.getRotateName(cClientLogic.newprefabname)),0, 27*sSettings.height/32);
                        drawnRotate = true;
                        break;
                    }
                }
                if(cClientLogic.selectedPrefabId.length() > 0 || cClientLogic.selecteditemid.length() > 0) {
                    g.drawString("[BACKSPACE] - DELETE SELECTED", 0, !drawnRotate ? 27 * sSettings.height / 32
                                                                                        : 25 * sSettings.height / 32);
                }
                g.drawString(String.format("[MOUSE_LEFT] - PLACE %s", newThingString), 0,
                        29*sSettings.height/32);
                g.drawString("[Esc] - TEST/EDIT ", 0,
                    31*sSettings.height/32);
            }
        }
        //console
        dFonts.setFontConsole(g);
        if(uiInterface.inconsole) {
            g.setColor(gColors.getFontColorFromName("scoreboardbg"));
            g.fillRect(0,0,sSettings.width,sSettings.height);
            g.setColor(gColors.getFontColorFromName("console"));
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
            is = new StringBuilder(gameTimeMillis % 500 > 250 ? is.toString() : String.format("%s_", is.toString()));
            g.drawString(String.format("console:~$ %s", xCon.instance().commandString),
                0,(xCon.instance().linesToShow+1)*sSettings.height/64);
            g.drawString(String.format("           %s", is.toString()), 0,
                (xCon.instance().linesToShow+1)*sSettings.height/64);
        }
        //big font
        dFonts.setFontNormal(g);
        //respawn msg
        //scoreboard
        if(showscore) {
            dScoreboard.showScoreBoard(g);
        }
        //loading
        if(sSettings.IS_CLIENT && !cClientLogic.maploaded) {
//            dFonts.drawCenteredString(g, "LOADING...", sSettings.width / 2, 9 * sSettings.height / 12);
            dFonts.drawRightJustifiedString(g, "LOADING...",
                    29 * sSettings.width / 30, 31*sSettings.height/32);
        }
        //echo messages
        if(gMessages.screenMessages.size() > 0) {
            for(int i = 0; i < gMessages.screenMessages.size(); i++) {
                String s = gMessages.screenMessages.get(i);
                g.setColor(Color.BLACK);
                g.drawString(s,3,24*sSettings.height/32-(gMessages.screenMessages.size()*(sSettings.height/32))
                        +(i*(sSettings.height/32))+3);
                dFonts.setFontColorNormal(g);
                g.drawString(s,0,24*sSettings.height/32-(gMessages.screenMessages.size()*(sSettings.height/32))
                        +(i*(sSettings.height/32)));
            }
        }
    }
}
