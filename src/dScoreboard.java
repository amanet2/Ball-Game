import java.awt.*;
import java.util.HashMap;

public class dScoreboard {
    public static void showScoreBoard(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.setColor(new Color(Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[0]),
                Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[1]),
                Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[2]),
                Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[3])));
        dFonts.drawCenteredString(g,
                cGameLogic.net_gamemode_texts[cVars.getInt("gamemode")].toUpperCase() + ": "
                        + cGameLogic.net_gamemode_descriptions[cVars.getInt("gamemode")],
                sSettings.width/2, 2*sSettings.height/30);
        g.setColor(new Color(Integer.parseInt(sVars.get("fontcolornormal").split(",")[0]),
                Integer.parseInt(sVars.get("fontcolornormal").split(",")[1]),
                Integer.parseInt(sVars.get("fontcolornormal").split(",")[2]),
                Integer.parseInt(sVars.get("fontcolornormal").split(",")[3])));
        g.drawString("["+ gScoreboard.scoresMap.size() + " players]",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                           Wins",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                       Score",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                                   Kills",
                sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                                               Ping",
                sSettings.width/4,5*sSettings.height/30);
        g.drawString("-----------------------------------------------------------",
                sSettings.width/4, 6*sSettings.height/30);
        int i = 0;
        int prevscore=-1000000;
        int prevplace = 0;
        String[] scoretoks = cVars.get("scoremap").split(":");
        HashMap<String, HashMap<String, Integer>> scoresMap = gScoreboard.scoresMap;
        for(String id : scoresMap.keySet()) {
            if(scoretoks.length > 0 && scoretoks.length == scoresMap.size()) {
                if(scoretoks[i].split("-")[0].length() > 0)
                    id = scoretoks[i].split("-")[0];
            }
            if(!nClient.instance().serverArgsMap.containsKey(id))
                continue;
            String playername = nClient.instance().serverArgsMap.get(id).get("name");
            String playercolor = nClient.instance().serverArgsMap.get(id).get("color");
            HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
            int playerwins = scoresMapIdMap.get("wins");
            int playerscore = scoresMapIdMap.get("score");
            int playerkills = scoresMapIdMap.get("kills");
            int playerping = scoresMapIdMap.get("ping");
            boolean isMe = false;
            if(id.equals(uiInterface.uuid)) {
                isMe = true;
                g.setColor(new Color(
                        Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[0]),
                        Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[1]),
                        Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[2]),
                        Integer.parseInt(sVars.get("fontcolorhighlight").split(",")[3])));
            }
            int place = i;
            if(playerscore == prevscore)
                place = prevplace;
            prevplace = place;
            prevscore = playerscore;
            String spectatorstring = "";
            if(eManager.getPlayerById(id) == null)
                spectatorstring = "[SPECTATE] ";
            g.drawString(String.format("%s%d. ", spectatorstring, place+1)
                            + playername, sSettings.width/4 - dFonts.getStringWidth(g, spectatorstring),
                    7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                           " + playerwins,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                       " + playerscore,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                                   " + playerkills,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                                               " + playerping,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            if(isMe) {
                g.setColor(new Color(
                        Integer.parseInt(sVars.get("fontcolornormal").split(",")[0]),
                        Integer.parseInt(sVars.get("fontcolornormal").split(",")[1]),
                        Integer.parseInt(sVars.get("fontcolornormal").split(",")[2]),
                        Integer.parseInt(sVars.get("fontcolornormal").split(",")[3])));
            }
            i++;
        }
    }
}
