import java.awt.*;
import java.util.*;

public class dScreenMessages {
    private static Image logoimg = gTextures.getGScaledImage(eManager.getPath("misc/logo.png"),
            sSettings.width, sSettings.height/3);
    static ArrayList<String> messagesOnScreen = new ArrayList<>();
    static Queue<Long> expireTimes = new LinkedList<>();

    public static void addMessage(String s) {
        messagesOnScreen.add(s);
        expireTimes.add(sSettings.gameTime + sSettings.screenMessageFadeTime);
    }

    private static void showInfo(Graphics g) {
        //start displaying
        dFonts.setFontSmall(g);
        //scale
        if(sSettings.showscale)
            dFonts.drawRightJustifiedString(g, "ZOOM:" + sSettings.zoomLevel, 63*sSettings.width/64, sSettings.height/64);
        //fps
        if(sSettings.showfps)
            dFonts.drawRightJustifiedString(g, "FPS:" + sSettings.fpsReport, 63*sSettings.width/64, 2*sSettings.height / 64);
        //client
        if(sSettings.showtick)
            dFonts.drawRightJustifiedString(g, "SHELL:" + sSettings.tickReport, 63*sSettings.width/64, 3 * sSettings.height / 64);
        //net
        if(sSettings.shownet) {
            dFonts.drawRightJustifiedString(g, "CLIENT_NET:" + sSettings.tickReportClient, 63*sSettings.width/64, 4 * sSettings.height / 64);
            dFonts.drawRightJustifiedString(g, "SERVER_NET:" + sSettings.tickReportServer, 63*sSettings.width/64, 5 * sSettings.height / 64);
            dFonts.drawRightJustifiedString(g, "SIMULATION:" + sSettings.tickReportSimulation, 63*sSettings.width/64, 6 * sSettings.height / 64);
            dFonts.drawRightJustifiedString(g, "PING:" + sSettings.clientPing, 63*sSettings.width/64, 7 * sSettings.height / 64);
        }
        if(sSettings.showcam) {
            //camera
            String camstring = String.format("Cam: %d,%d", (int) gCamera.coords[0], (int) gCamera.coords[1]);
            dFonts.drawRightJustifiedString(g, camstring,63*sSettings.width/64, 8 * sSettings.height / 64);
        }
        if(sSettings.showmouse) {
            int[] mc = xMain.shellLogic.getMouseCoordinates();
            if(sSettings.show_mapmaker_ui)
                dFonts.drawRightJustifiedString(g, String.format("Mouse: %d,%d", xMain.shellLogic.getPlaceObjCoords()[0],
                        xMain.shellLogic.getPlaceObjCoords()[1]),63*sSettings.width/64,9*sSettings.height/64);
            else
                dFonts.drawRightJustifiedString(g, String.format("Mouse: %d,%d",eUtils.unscaleInt(mc[0]) + (int) gCamera.coords[0],
                        eUtils.unscaleInt(mc[1]) + (int) gCamera.coords[1]),63*sSettings.width/64,9*sSettings.height/64);
        }
        if(sSettings.showplayer && xMain.shellLogic.getUserPlayer() != null) {
            dFonts.drawRightJustifiedString(g, String.format("Player: %d,%d",
                            xMain.shellLogic.getUserPlayer().coords[0],
                            xMain.shellLogic.getUserPlayer().coords[1]),
                    63*sSettings.width/64,10*sSettings.height/64);
        }
    }

    public static void displayScreenMessages(Graphics g, long gameTimeMillis) {
        //expired msgs
        if(expireTimes.size() > 0 && expireTimes.peek() != null && expireTimes.peek() < gameTimeMillis) {
            messagesOnScreen.remove(0);
            expireTimes.remove();
        }
        //ingame messages
        dFonts.setFontColor(g, "clrf_normal");
        if(sSettings.inplay)
            drawHUD(g);
        //timer
        dFonts.setFontLarge(g);
        if(sSettings.inplay && sSettings.clientMapLoaded) {
            String timeString = eUtils.getTimeString(sSettings.clientTimeLeft);
            g.setColor(Color.BLACK);
            dFonts.drawCenteredString(g, timeString, sSettings.width / 2 + 1, sSettings.height / 12 + 1);
            dFonts.setFontLarge(g);
            g.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
            dFonts.drawCenteredString(g, timeString, sSettings.width / 2, sSettings.height / 12);
        }
        //menus
        dFonts.setFontNormal(g);
        if(!sSettings.inplay) {
            if(!sSettings.show_mapmaker_ui) {
                showPauseMenu(g);
            }
            else if(sSettings.clientMapLoaded){
                String newThingString = sSettings.clientNewPrefabName;
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
                if(uiEditorMenus.newitemname.length() > 0)
                    newThingString = uiEditorMenus.newitemname;
                boolean drawnRotate = false;
                String[] rotates = {"_000", "_090", "_180", "_270"};
                ArrayList<String> rotatesList = new ArrayList<>(Arrays.asList(rotates));
                for(String s : rotatesList) {
                    if(sSettings.clientNewPrefabName.contains(s)) {
                        g.drawString(String.format("[R] - ROTATE %s",
                                uiEditorMenus.getRotateName(sSettings.clientNewPrefabName)),0, 27*sSettings.height/32);
                        drawnRotate = true;
                        break;
                    }
                }

                if(sSettings.clientSelectedPrefabId.length() > 0 || sSettings.clientSelectedItemId.length() > 0) {
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
        if(sSettings.inconsole) {
            dFonts.setFontColor(g, "clrf_scoreboardbg");
            g.fillRect(0,0,sSettings.width,sSettings.height);
            dFonts.setFontColor(g, "clrf_console");
            g.fillRect(0,0,sSettings.width, (xMain.shellLogic.console.linesToShow + 2) * sSettings.height/64);
            dFonts.setFontColor(g, "clrf_normal");
            int ctr = 0;
            for(int i = xMain.shellLogic.console.linesToShowStart;
                i < xMain.shellLogic.console.linesToShowStart + xMain.shellLogic.console.linesToShow; i++) {
                int dd = 1;
                if(i == xMain.shellLogic.console.linesToShowStart && xMain.shellLogic.console.linesToShowStart > 0) {
                    g.drawString(String.format("--- (%d) scroll up ---", xMain.shellLogic.console.linesToShowStart),
                        0, (ctr + 1) * sSettings.height / 64);
                    dd = 0;
                }
                if(i == xMain.shellLogic.console.linesToShowStart + xMain.shellLogic.console.linesToShow-1
                    && xMain.shellLogic.console.linesToShowStart
                    < xMain.shellLogic.console.stringLines.size() - xMain.shellLogic.console.linesToShow) {
                    g.drawString(String.format("--- (%d) scroll down ---",
                        xMain.shellLogic.console.stringLines.size()- xMain.shellLogic.console.linesToShowStart
                            - xMain.shellLogic.console.linesToShow),
                        0, (ctr + 1) * sSettings.height / 64);
                    dd = 0;
                }
                if(dd != 0) {
                    if(i < 1024 && xMain.shellLogic.console.stringLines.size() > i) {
                        String ds = xMain.shellLogic.console.stringLines.get(i);
                        if(ds == null)
                            ds = "null";
                        g.drawString(ds, 0, (ctr + 1) * sSettings.height / 64);
                    }
                }
                ctr++;
            }
            StringBuilder is = new StringBuilder();
            is.append(" ".repeat(Math.max(0, xMain.shellLogic.console.cursorIndex)));
            is = new StringBuilder(gameTimeMillis % 500 > 250 ? is.toString() : String.format("%s_", is));
            g.drawString(String.format("console:~$ %s", xMain.shellLogic.console.commandString),
                0,(xMain.shellLogic.console.linesToShow+1)*sSettings.height/64);
            g.drawString(String.format("           %s", is), 0,
                (xMain.shellLogic.console.linesToShow+1)*sSettings.height/64);
        }
        //wip notice -> needs to be transparent
        dFonts.setFontNormal(g);
        dFonts.setFontColor(g, "clrf_normaltransparent");
        dFonts.drawRightJustifiedString(g, "WORK IN PROGRESS", 63*sSettings.width/64, 31*sSettings.height/32);
        //big font
        dFonts.setFontNormal(g);
        //scoreboard
        if(sSettings.showscore)
            showScoreBoard(g);
        //loading
        if(sSettings.IS_CLIENT && !sSettings.clientMapLoaded)
            dFonts.drawCenteredString(
                    g,
                    String.format("%d ball %s - %s",
                            new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot).keys().size(),
                            sSettings.clientGameModeTitle,
                            sSettings.clientGameModeText
                    ),
                    sSettings.width / 2,
                    sSettings.height / 2
            );
        //echo messages
        if(!messagesOnScreen.isEmpty()) {
            for(int i = 0; i < messagesOnScreen.size(); i++) {
                String s = messagesOnScreen.get(i);
                dFonts.setFontColor(g, "clrf_normal");
                // look for hashtag color codes here
                StringBuilder ts = new StringBuilder();
                for(String word : s.split(" ")) {
                    if(word.contains("#")) {
                        if(word.split("#").length != 2)
                            ts.append(word).append(" ");
                        else if(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")) != null){
                            g.setColor(Color.BLACK);
                            g.drawString(word.split("#")[0]+" ",
                                    dFonts.getStringWidth(g, ts.toString())+1,
                                    24*sSettings.height/32-(messagesOnScreen.size()*(sSettings.height/32))
                                            +(i*(sSettings.height/32))+1);
                            g.setColor(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")));
                            g.drawString(word.split("#")[0]+" ",
                                    dFonts.getStringWidth(g, ts.toString()),
                                    24*sSettings.height/32-(messagesOnScreen.size()*(sSettings.height/32))
                                            +(i*(sSettings.height/32)));
                            dFonts.setFontColor(g, "clrf_normal");
                            ts.append(word.split("#")[0]).append(word.contains(":") ? ": " : " ");
                            continue;
                        }
                    }
                    g.setColor(Color.BLACK);
                    g.drawString(word+" ",
                            dFonts.getStringWidth(g, ts.toString())+1,
                            24*sSettings.height/32-(messagesOnScreen.size()*(sSettings.height/32))
                                    +(i*(sSettings.height/32))+1);
                    dFonts.setFontColor(g, "clrf_normal");
                    g.drawString(word+" ",
                            dFonts.getStringWidth(g, ts.toString()),
                            24*sSettings.height/32-(messagesOnScreen.size()*(sSettings.height/32))
                            +(i*(sSettings.height/32)));
                    ts.append(word).append(" ");
                }
            }
        }
        //big font
        dFonts.setFontNormal(g);
        //say
        if(xMain.shellLogic.enteringMessage)
            g.drawString(String.format("%s: %s", xMain.shellLogic.prompt, xMain.shellLogic.msgInProgress),
                    0,25 * sSettings.height/32);
        //show fps, etc.
        if(sSettings.showinfo)
            showInfo(g);
    }

    public static void refreshLogos() {
        logoimg = gTextures.getGScaledImage(eManager.getPath("misc/logo.png"), sSettings.width, sSettings.height/3);
    }

    private static void showPauseMenu(Graphics g) {
        xMain.shellLogic.getUIMenuItemUnderMouse();
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
//        g.drawImage(logoimg,0,0,null);
        dFonts.setFont(g, dFonts.fontSuper);
        dFonts.setFontColor(g, "clrp_"+sSettings.clientPlayerColor);
        dFonts.drawCenteredString(g, "BALLMASTER", sSettings.width/2, sSettings.height/4);
        dFonts.setFontNormal(g);
        StringBuilder crumbString = new StringBuilder(uiMenus.menuSelection[uiMenus.selectedMenu].title);
        int crumbParent = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
        while(crumbParent > -1) {
            crumbString.insert(0, uiMenus.menuSelection[crumbParent].title + "/");
            crumbParent = uiMenus.menuSelection[crumbParent].parentMenu;
        }
        int alignX = sSettings.width/2;
        int alignY = 21*sSettings.height/60;
        g.setColor(Color.WHITE);
        g.drawLine(0, alignY + sSettings.height/60, sSettings.width, alignY + sSettings.height/60);
        g.setColor(Color.GRAY);
        dFonts.drawCenteredString(g,crumbString.toString(), sSettings.width / 2, alignY);
        dFonts.setFontColor(g, "clrf_normal");
        int ctr = 0;
        int sel = 0;
        for(uiMenuItem i : uiMenus.menuSelection[uiMenus.selectedMenu].items){
            if(uiMenus.selectedMenu == uiMenus.MENU_CONTROLS) {
                String action = i.text.split(":")[0];
                String input = i.text.split(":")[1];
                dFonts.drawRightJustifiedString(g," "+action,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
                g.drawString( " "+input,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
            }
            else if(uiMenus.selectedMenu != uiMenus.MENU_CREDITS && ctr == uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem) {
                sel = 1;
                if(uiMenus.selectedMenu == uiMenus.MENU_COLOR && !xMain.shellLogic.console.ex("cl_setvar clrp_" + i.text).contains("null"))
                    dFonts.setFontColor(g, "clrp_" + i.text);
                else
                    dFonts.setFontColor(g, "clrp_" + sSettings.clientPlayerColor);
                dFonts.drawCenteredString(g,i.text, alignX,12*sSettings.height/30+ctr*sSettings.height/30);
                dFonts.setFontColor(g, "clrf_normal");
                if(xMain.shellLogic.frame.getCursor() != Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                    xMain.shellLogic.frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            else {
                if(uiMenus.selectedMenu == uiMenus.MENU_MAIN && !sSettings.IS_CLIENT && ctr == 2)
                    g.setColor(Color.GRAY);
                dFonts.drawCenteredString(g, i.text, alignX,12*sSettings.height/30+ctr*sSettings.height/30);
                dFonts.setFontColor(g, "clrf_normal");
            }
            ctr++;
        }
        if(uiMenus.gobackSelected) {
            dFonts.setFontColor(g, "clrp_" + sSettings.clientPlayerColor);
            if(xMain.shellLogic.frame.getCursor() != Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                xMain.shellLogic.frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        if(!uiMenus.gobackSelected && sel == 0 && xMain.shellLogic.frame.getCursor() != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
            xMain.shellLogic.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        g.drawString("←",9 * sSettings.width / 32, alignY);
        g.drawRect(9 * sSettings.width / 32, alignY - sSettings.width/60, sSettings.width / 32, sSettings.height/30);
    }

    private static void drawHUD(Graphics g) {
        if(!sSettings.IS_CLIENT || !sSettings.clientMapLoaded)
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        nState userState = clStateMap.get(sSettings.uuid);
        if(userState == null)
            return;
        int ctr = 0;
        boolean smallMode = clStateMap.keys().size() > 7;
        int hpbarwidth = smallMode ? sSettings.width/16 : sSettings.width/8;
        int marginX = sSettings.width/2 - clStateMap.keys().size()*(hpbarwidth/2 + sSettings.width/128);
        String[] sortedScoreIds = getSortedScoreIds(clStateMap, 1);
        for(String id : sortedScoreIds) {
            nState clState = clStateMap.get(id);
            int alignX = marginX + ctr*(hpbarwidth + sSettings.width/64);
            Color color = gColors.getColorFromName("clrp_" + clState.get("color"));
            //healthbar
            g.setColor(Color.black);
            g.fillRect(alignX+1,smallMode ? 29*sSettings.height/32 : 28 * sSettings.height/32 + 1,hpbarwidth, sSettings.height/24);
            g.setColor(color);
            if(Integer.parseInt(clState.get("hp")) > 0 && xMain.shellLogic.getPlayerById(id) != null)
                g.fillRect(alignX, smallMode ? 29*sSettings.height/32 : 28 * sSettings.height/32,
                        hpbarwidth*Integer.parseInt(clState.get("hp"))/ sSettings.clientMaxHP,
                        sSettings.height/24);
            //small circle next to name
            int[] bounds = { alignX - 3,
                    smallMode ? 56*sSettings.height/64 : 53*sSettings.height/64,
                    smallMode ? sSettings.height/64 : sSettings.height/32,
                    smallMode ? sSettings.height/64 : sSettings.height/32};
            g.setColor(Color.BLACK);
            g.fillOval(bounds[0]+1, bounds[1]+1, bounds[2], bounds[3]);
            g.setColor(color);
            if(xMain.shellLogic.clientScene.getPlayerById(id) == null)
                g.setColor(Color.GRAY);
            g.fillOval(bounds[0], bounds[1], bounds[2], bounds[3]);
            //small triangle above name
            if(id.equals(sSettings.uuid)) {
                Polygon triangle = new Polygon(
                        new int[]{bounds[0], bounds[0] + bounds[2], bounds[0] + bounds[2] / 2},
                        new int[]{bounds[1] - bounds[3]/2, bounds[1] - bounds[3]/2, bounds[1]},
                        3);
                Polygon shadow = new Polygon(
                        new int[]{bounds[0]+1, bounds[0] + bounds[2]+1, bounds[0] + bounds[2] / 2 + 1},
                        new int[]{bounds[1] - bounds[3]/2 + 1, bounds[1] - bounds[3]/2 + 1, bounds[1] + 1},
                        3);
                g.setColor(Color.BLACK);
                g.fillPolygon(shadow);
                g.setColor(color);
                if(xMain.shellLogic.clientScene.getPlayerById(id) == null)
                    g.setColor(Color.GRAY);
                g.fillPolygon(triangle);

            }
            //name
            if(smallMode)
                dFonts.setFontSmall(g);
            else
                dFonts.setFontNormal(g);
            g.setColor(Color.BLACK);
            g.drawString(clState.get("name"), alignX + bounds[2] + 1, smallMode ? 57*sSettings.height/64 : 55*sSettings.height/64 + 1);
            g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
            if(xMain.shellLogic.clientScene.getPlayerById(id) == null)
                g.setColor(Color.GRAY);
            g.drawString(clState.get("name"), alignX + bounds[2], smallMode ? 57*sSettings.height/64 : 55*sSettings.height/64);
            //score
            if(smallMode)
                dFonts.setFontNormal(g);
            else
                dFonts.setFontLarge(g);
            if(clState.contains("score")) {
                g.setColor(Color.BLACK);
                g.drawString(clState.get("score").split(":")[1],
                        marginX + ctr*(hpbarwidth + sSettings.width/64) + 1, 63*sSettings.height/64 + 1);
                g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
                if(xMain.shellLogic.clientScene.getPlayerById(id) == null)
                    g.setColor(Color.GRAY);
                g.drawString(clState.get("score").split(":")[1],
                        marginX + ctr*(hpbarwidth + sSettings.width/64), 63*sSettings.height/64);
            }
            ctr++;
        }
    }

    private static String[] getSortedScoreIds(nStateMap clStateMap, int scoreIndex) {
        StringBuilder sortedScoreIds = new StringBuilder();
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            int topscore = -1;
            String topid = "";
            for (String id : clStateMap.keys()) {
                if(!sortedScoreIds.toString().contains(id) && clStateMap.get(id).contains("score")) {
                    if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[scoreIndex]) > topscore) {
                        topscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[scoreIndex]);
                        topid = id;
                        sorted = false;
                    }
                }
            }
            sortedScoreIds.append(topid).append(",");
        }
        return sortedScoreIds.toString().split(",");
    }

    private static void showScoreBoard(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        int spriteRad = sSettings.height/30;
        int alignX = 3*sSettings.width/8;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColor(g, "clrf_normal");
        dFonts.drawCenteredString(g, String.format("%d ball %s - %s", clStateMap.keys().size(), sSettings.clientGameModeTitle, sSettings.clientGameModeText), sSettings.width/2,5*spriteRad);
        g.setColor(Color.BLACK);
        g.drawLine(3*sSettings.width/8+1, 11*sSettings.height/60+1, 5*sSettings.width/8+1, 11*sSettings.height/60+1);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawLine(3*sSettings.width/8, 11*sSettings.height/60, 5*sSettings.width/8, 11*sSettings.height/60);

        String[] sortedScoreIds = getSortedScoreIds(clStateMap, 0);
        int ctr = 0;
        boolean isMe = false;
        for(String id : sortedScoreIds) {
            if(id.equals(sSettings.uuid))
                isMe = true;
            String hudName = clStateMap.get(id).get("name");
            int coordy = 7 * sSettings.height / 30 + ctr * sSettings.height / 30;
            String ck = clStateMap.get(id).get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            if(xMain.shellLogic.clientScene.getPlayerById(id) == null)
                color = Color.GRAY;
            dFonts.drawScoreBoardPlayerLine(g, hudName, alignX, coordy, color);
            if(isMe) {
                g.setColor(Color.BLACK);
                dFonts.drawRightJustifiedString(g, "*", alignX, coordy);
                g.setColor(color);
                dFonts.drawRightJustifiedString(g, "*", alignX, coordy);
            }
            g.setColor(Color.BLACK);
            dFonts.drawRightJustifiedString(g, clStateMap.get(id).get("score").split(":")[0],
                    alignX + sSettings.width/4 + 1, coordy + 1);
            g.setColor(color);
            dFonts.drawRightJustifiedString(g, clStateMap.get(id).get("score").split(":")[0],
                    alignX + sSettings.width/4, coordy);
            dFonts.setFontColor(g, "clrf_normal");
            if(isMe)
                isMe = false;
            ctr++;
        }
    }
}
