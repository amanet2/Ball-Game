import javax.swing.*;
import java.awt.*;

public class dMenus {
    private static Image coverimg = new ImageIcon(
        String.format("%s/%s", sVars.get("datapath"), sVars.get("coverpath")
        )).getImage().getScaledInstance(sSettings.width, sSettings.height, Image.SCALE_FAST);
    private static Image logoimg = new ImageIcon(
            String.format("%s/%s", sVars.get("datapath"), sVars.get("logopath")
            )).getImage().getScaledInstance(sSettings.width, sSettings.height/3, Image.SCALE_FAST);

    public static void showPauseMenu(Graphics g) {
        if(eUtils.resolutionChanged()) {
            logoimg = new ImageIcon(
                    String.format("%s/%s", sVars.get("datapath"), sVars.get("logopath")
                    )).getImage().getScaledInstance(sSettings.width, sSettings.height/3, Image.SCALE_FAST);
            coverimg = new ImageIcon(
                    String.format("%s/%s", sVars.get("datapath"), sVars.get("coverpath")
                    )).getImage().getScaledInstance(sSettings.width, sSettings.height, Image.SCALE_FAST);
        }
        cScripts.getUIMenuItemUnderMouse();
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(logoimg,0,0,null);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[3])));
        dScreenMessages.drawCenteredString(g, uiMenus.menuSelection[uiMenus.selectedMenu].title,
            sSettings.width/2,10*sSettings.height/30);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
        dScreenMessages.drawCenteredString(g, "----------------",sSettings.width/2,11*sSettings.height/30);
        int ctr = 0;
        int sel = 0;
        for(uiMenuItem i : uiMenus.menuSelection[uiMenus.selectedMenu].items){
            if(ctr == uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem) {
                sel = 1;
                g.setColor(new Color(Integer.parseInt(xCon.ex("textcolorbonus").split(",")[0]),
                        Integer.parseInt(xCon.ex("textcolorbonus").split(",")[1]),
                        Integer.parseInt(xCon.ex("textcolorbonus").split(",")[2]),
                        Integer.parseInt(xCon.ex("textcolorbonus").split(",")[3])));
                dScreenMessages.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
                g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
                if(oDisplay.instance().frame.getCursor() != Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                    oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            else {
                dScreenMessages.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
            }
            ctr++;
        }
        if(sel == 0 && oDisplay.instance().frame.getCursor() != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
            oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void showControlsMenu(Graphics g) {
        cScripts.getUIMenuItemUnderMouse();
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(logoimg,0,0,null);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[3])));
        dScreenMessages.drawCenteredString(g, uiMenus.menuSelection[uiMenus.selectedMenu].title,
            sSettings.width/2,10*sSettings.height/30);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
        dScreenMessages.drawCenteredString(g, "----------------",sSettings.width/2,11*sSettings.height/30);
        dScreenMessages.drawCenteredString(g,
                "- ACTION -",sSettings.width/3,12*sSettings.height/30);
        g.drawString("   - KEY/BUTTON -",2*sSettings.width/3,12*sSettings.height/30);
        dScreenMessages.drawCenteredString(g,
                "",sSettings.width/4,13*sSettings.height/30);
        dScreenMessages.drawCenteredString(g,
                "",2*sSettings.width/3,13*sSettings.height/30);
        int ctr = 0;
        for(uiMenuItem i : uiMenus.menuSelection[uiMenus.selectedMenu].items){
            String action = i.text.split(":")[0];
            String input = i.text.split(":")[1];
            if(ctr == uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem) {
                g.drawString(">"+action,
                        sSettings.width/4, 13*sSettings.height/30+ctr%16*sSettings.height/30);
                g.drawString(">"+input,
                        2*sSettings.width/3, 13*sSettings.height/30+ctr%16*sSettings.height/30);
            }
            else {
                g.drawString(" "+action,
                    sSettings.width/4, 13*sSettings.height/30+ctr%16*sSettings.height/30);
                g.drawString(" "+input,
                        2*sSettings.width/3, 13*sSettings.height/30+ctr%16*sSettings.height/30);
            }
            ctr++;
        }
    }

    public static void showCreditsMenu(Graphics g) {
        if(eUtils.resolutionChanged()) {
            logoimg = new ImageIcon(
                    String.format("%s/%s", sVars.get("datapath"), sVars.get("logopath")
                    )).getImage().getScaledInstance(sSettings.width, sSettings.height/3, Image.SCALE_FAST);
            coverimg = new ImageIcon(
                    String.format("%s/%s", sVars.get("datapath"), sVars.get("coverpath")
                    )).getImage().getScaledInstance(sSettings.width, sSettings.height, Image.SCALE_FAST);
        }
        cScripts.getUIMenuItemUnderMouse();
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(coverimg,0,0,null);
        //copyright notice
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
        g.drawString("by Anthony Manetti",sSettings.width - sSettings.width / 3, sSettings.height - sSettings.height / 15);
        g.drawString("Venmo @Anthony-Manetti",sSettings.width - sSettings.width / 3, sSettings.height - sSettings.height / 30);
    }

    public static void showScoreBoard(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[3])));
        dScreenMessages.drawCenteredString(g,String.format("%s | %s%s",eManager.currentMap.mapName,
                cGameMode.net_gamemode_texts[cVars.getInt("gamemode")],cVars.isOne("gameteam") ? "-TEAM" : ""),
            sSettings.width/2,
            sSettings.height/30);
        dScreenMessages.drawCenteredString(g, cVars.get("scorelimit") + "pts to win | "
                        + eUtils.getTimeString(cVars.getLong("timeleft")) + " remaining",
            sSettings.width/2, 2*sSettings.height/30);
        dScreenMessages.drawCenteredString(g, cGameMode.net_gamemode_descriptions[cVars.getInt("gamemode")],
                sSettings.width/2,
                3*sSettings.height/30);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
        dScreenMessages.drawCenteredString(g,"--------",
            sSettings.width/2,
            4*sSettings.height/30);
        g.drawString("["+nServer.clientArgsMap.size() + " players]",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                           Wins",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                       Score",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                                   Kills",
                sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                                               Ping",
                sSettings.width/4,5*sSettings.height/30);

        int i = 0;
        for(String id : nServer.scoresMap.keySet()) {
            String playername = cGameLogic.getPlayerById(id).get("name");
            String playercolor = cGameLogic.getPlayerById(id).get("color");
            int playerwins = nServer.scoresMap.get(id).get("wins");
            int playerscore = nServer.scoresMap.get(id).get("score");
            int playerkills = nServer.scoresMap.get(id).get("kills");
            int playerping = nServer.scoresMap.get(id).get("ping");
            if(id.equals(cGameLogic.getUserPlayer().get("id"))) {
                g.setColor(new Color(
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[0]),
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[1]),
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[2]),
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[3])));
            }
            g.drawString((cVars.isOne("gameteam") ? "(" +playercolor+")" : "") + playername, sSettings.width/4,
                    6 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                           " + playerwins,
                    sSettings.width/4,6 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                       " + playerscore,
                    sSettings.width/4,6 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                                   " + playerkills,
                    sSettings.width/4,6 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                                               " + playerping,
                    sSettings.width/4,6 * sSettings.height / 30 + i * sSettings.height / 30);
            if(id.equals(cGameLogic.getUserPlayer().get("id"))) {
                g.setColor(new Color(
                        Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                        Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                        Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                        Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
            }
            i++;
        }
    }
}
