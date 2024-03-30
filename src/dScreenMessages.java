import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public class dScreenMessages {
    static ArrayList<String> messagesOnScreen = new ArrayList<>();
    static Queue<Long> expireTimes = new LinkedList<>();

    public static void addMessage(String s) {
        messagesOnScreen.add(s);
        expireTimes.add(sSettings.gameTime + sSettings.screenMessageFadeTime);
    }

    public static void checkExpiredMessages() {
        //expired msgs
        if(expireTimes.size() > 0) {
            if(expireTimes.peek() != null && expireTimes.peek() < sSettings.gameTime) {
                messagesOnScreen.remove(0);
                expireTimes.remove();
            }
        }
    }

    public static void displayScreenMessages(Graphics g, long gameTimeMillis) {
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
            String camstring = String.format("Cam: %d,%d",
                    (int) gCamera.coords[0], (int) gCamera.coords[1]);
            dFonts.drawRightJustifiedString(g, camstring,63*sSettings.width/64, 8 * sSettings.height / 64);
        }
        if(sSettings.showmouse) {
            int[] mc = uiInterface.getMouseCoordinates();
            if(sSettings.show_mapmaker_ui)
                dFonts.drawRightJustifiedString(g, String.format("Mouse: %d,%d", uiInterface.getPlaceObjCoords()[0],
                        uiInterface.getPlaceObjCoords()[1]),63*sSettings.width/64,9*sSettings.height/64);
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
        //ingame messages
        dFonts.setFontColor(g, "clrf_normal");
        if(sSettings.inplay)
            dHUD.drawHUD(g);
        //timer
        dFonts.setFontLarge(g);
        if(sSettings.inplay && sSettings.clientMapLoaded) {
            g.setColor(Color.BLACK);
            g.drawString(eUtils.getTimeString(sSettings.clientTimeLeft), sSettings.width / 128 + 2, sSettings.height / 12 + 2);
            dFonts.setFontLarge(g);
            g.drawString(eUtils.getTimeString(sSettings.clientTimeLeft), sSettings.width / 128, sSettings.height / 12);
        }
        //menus
        dFonts.setFontNormal(g);
        if(!sSettings.inplay) {
            if(!sSettings.show_mapmaker_ui) {
                dMenus.showPauseMenu(g);
                if(uiMenus.gobackSelected)
                    g.setColor(Color.WHITE);
                g.drawString("[Esc] GO BACK",0,31*sSettings.height/32);
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
            dHUD.showScoreBoard(g);
        //loading
        if(sSettings.IS_CLIENT && !sSettings.clientMapLoaded)
            dFonts.drawCenteredString(g, sSettings.clientGameModeTitle + " - " + sSettings.clientGameModeText, sSettings.width/2, sSettings.height/2);
        //echo messages
        if(messagesOnScreen.size() > 0) {
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
                                    dFonts.getStringWidth(g, ts.toString())+3,
                                    24*sSettings.height/32-(messagesOnScreen.size()*(sSettings.height/32))
                                            +(i*(sSettings.height/32))+3);
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
                            dFonts.getStringWidth(g, ts.toString())+3,
                            24*sSettings.height/32-(messagesOnScreen.size()*(sSettings.height/32))
                                    +(i*(sSettings.height/32))+3);
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
    }
}
