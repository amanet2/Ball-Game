import java.awt.Color;
import java.awt.Graphics;

public class dScoreboard {
    public static void showScoreBoard(Graphics g) {
        nStateMap clStateMap = nClient.instance().clientStateMap;
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColor(g, "clrf_highlight");
        dFonts.drawCenteredString(g,
                cGameLogic.net_gamemode_strings[cClientLogic.gamemode][0].toUpperCase() + ": "
                        + cGameLogic.net_gamemode_strings[cClientLogic.gamemode][1],
                sSettings.width/2, 2*sSettings.height/30);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawString(clStateMap.keys().size() + " players",
                sSettings.width/3,5*sSettings.height/30);
        g.drawString("                           Wins",sSettings.width/3,5*sSettings.height/30);
        g.drawString("                                       Score",sSettings.width/3,5*sSettings.height/30);
        g.drawString("_______________________",
                sSettings.width/3, 11*sSettings.height/60);

        StringBuilder sortedScoreIds = new StringBuilder();
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            int topscore = -1;
            String topid = "";
            for (String id : clStateMap.keys()) {
                if(!sortedScoreIds.toString().contains(id) && clStateMap.get(id).contains("score")) {
                    if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]) > topscore) {
                        topscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]);
                        topid = id;
                        sorted = false;
                    }
                }
            }
            sortedScoreIds.append(topid).append(",");
        }
        int ctr = 0;
        int place = 1;
        int prevscore = -1;
        boolean isMe = false;
        for(String id : sortedScoreIds.toString().split(",")) {
            String spectatorstring = "";
            if(cClientLogic.getPlayerById(id) == null)
                spectatorstring = "[SPECTATE] ";
            if(id.equals(uiInterface.uuid)) {
                isMe = true;
            }
            if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]) < prevscore)
                place++;
            String hudName = String.format("%s%d. ", spectatorstring, place) + clStateMap.get(id).get("name");
            int coordx = sSettings.width/3 - dFonts.getStringWidth(g, spectatorstring);
            int coordy = 7 * sSettings.height / 30 + ctr * sSettings.height / 30;
            int height = sSettings.height / 30;
            String spaceStringA = "                                       ";
            String ck = clStateMap.get(id).get("color");
            Color color = gColors.instance().getColorFromName("clrp_" + ck);
            dFonts.drawPlayerNameHud(g, hudName, coordx, coordy, color);
            g.setColor(color);
            if(isMe)
                g.drawRect(coordx - dFonts.getStringWidth(g, hudName)/2, coordy - height,
                        dFonts.getStringWidth(g, hudName + spaceStringA + "  "), dFonts.getStringHeight(g, hudName));
            g.drawString("                           "
                            + clStateMap.get(id).get("score").split(":")[0],
                    sSettings.width/3,7 * sSettings.height / 30 + ctr * sSettings.height / 30);
            g.drawString("                                       "
                            + clStateMap.get(id).get("score").split(":")[1],
                    sSettings.width/3,7 * sSettings.height / 30 + ctr * sSettings.height / 30);
            dFonts.setFontColor(g, "clrf_normal");
            if(isMe)
                isMe = false;
            ctr++;
            prevscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]);
        }
    }
}
