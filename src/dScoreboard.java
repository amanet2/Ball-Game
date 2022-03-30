import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class dScoreboard {
    public static void showScoreBoard(Graphics g) {
        g.setColor(gColors.getFontColorFromName("scoreboardbg"));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColorHighlight(g);
        dFonts.drawCenteredString(g,
                cGameLogic.net_gamemode_texts[cClientLogic.gamemode].toUpperCase() + ": "
                        + cGameLogic.net_gamemode_descriptions[cClientLogic.gamemode],
                sSettings.width/2, 2*sSettings.height/30);
        dFonts.setFontColorNormal(g);
        g.drawString("["+ (nClient.instance().serverArgsMap.size()-1) + " players]",
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
            for (String id : nClient.instance().serverArgsMap.keySet()) {
                if(!id.equals("server") && !sortedScoreIds.toString().contains(id)
                && nClient.instance().serverArgsMap.get(id).containsKey("score")) {
                    if(Integer.parseInt(nClient.instance().serverArgsMap.get(id).get("score").split(":")[1])
                    > topscore) {
                        topscore = Integer.parseInt(nClient.instance().serverArgsMap.get(id).get("score").split(":")[1]);
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
                dFonts.setFontColorHighlight(g);
            }
            if(Integer.parseInt(nClient.instance().serverArgsMap.get(id).get("score").split(":")[1]) < prevscore)
                place++;
            g.drawString(String.format("%s%d. ", spectatorstring, place)
                            + nClient.instance().serverArgsMap.get(id).get("name"),
                    sSettings.width/3 - dFonts.getStringWidth(g, spectatorstring),
                    7 * sSettings.height / 30 + ctr * sSettings.height / 30);
            g.drawString("                           "
                            + nClient.instance().serverArgsMap.get(id).get("score").split(":")[0],
                    sSettings.width/3,7 * sSettings.height / 30 + ctr * sSettings.height / 30);
            g.drawString("                                       "
                            + nClient.instance().serverArgsMap.get(id).get("score").split(":")[1],
                    sSettings.width/3,7 * sSettings.height / 30 + ctr * sSettings.height / 30);
            if(isMe) {
                dFonts.setFontColorNormal(g);
                isMe = false;
            }
            ctr++;
            prevscore = Integer.parseInt(nClient.instance().serverArgsMap.get(id).get("score").split(":")[1]);
        }
    }
}
