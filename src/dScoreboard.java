import java.awt.*;
import java.util.HashMap;

public class dScoreboard {
    public static void showScoreBoard(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[3])));
        dScreenMessages.drawCenteredString(g,
                cGameMode.net_gamemode_texts[cVars.getInt("gamemode")].toUpperCase() + ": "
                        + cGameMode.net_gamemode_descriptions[cVars.getInt("gamemode")],
                sSettings.width/2, 2*sSettings.height/30);
        g.setColor(new Color(Integer.parseInt(xCon.ex("textcolornormal").split(",")[0]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[1]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[2]),
                Integer.parseInt(xCon.ex("textcolornormal").split(",")[3])));
//        dScreenMessages.drawRightJustifiedString(g, eManager.currentMap.mapName,
//                14*sSettings.width/60, 5*sSettings.height/30);
        g.drawString("["+nServer.clientArgsMap.size() + " players]",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                           Wins",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                       Score",sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                                   Kills",
                sSettings.width/4,5*sSettings.height/30);
        g.drawString("                                                               Ping",
                sSettings.width/4,5*sSettings.height/30);
        g.drawString("-------------------------------------------------------------------",
                sSettings.width/4, 6*sSettings.height/30);
        int i = 0;
        int prevscore=-1000000;
        int prevplace = 0;
        String[] scoretoks = cVars.get("scoremap").split(":");
        HashMap<String, HashMap<String, Integer>> scoresMap = cScoreboard.scoresMap;
        for(String id : scoresMap.keySet()) {
            if(scoretoks.length > 0 && scoretoks.length == scoresMap.size()) {
                if(scoretoks[i].split("-")[0].length() > 0)
                    id = scoretoks[i].split("-")[0];
            }
            String playername = cGameLogic.getPlayerById(id).get("name");
            String playercolor = cGameLogic.getPlayerById(id).get("color");
            HashMap<String, Integer> scoresMapIdMap = scoresMap.get(id);
            int playerwins = scoresMapIdMap.get("wins");
            int playerscore = scoresMapIdMap.get("score");
            int playerkills = scoresMapIdMap.get("kills");
            int playerping = scoresMapIdMap.get("ping");
            if(id.equals(cGameLogic.userPlayer().get("id"))) {
                g.setColor(new Color(
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[0]),
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[1]),
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[2]),
                        Integer.parseInt(xCon.ex("textcolorhighlight").split(",")[3])));
            }
            int place = i;
            if(playerscore == prevscore)
                place = prevplace;
            prevplace = place;
            prevscore = playerscore;
            g.drawString(String.format("%d. ", place+1)
                            + (cVars.isOne("gameteam") ? "(" +playercolor+")" : "")
                            + playername, sSettings.width/4,
                    7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                           " + playerwins,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                       " + playerscore,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                                   " + playerkills,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            g.drawString("                                                               " + playerping,
                    sSettings.width/4,7 * sSettings.height / 30 + i * sSettings.height / 30);
            if(id.equals(cGameLogic.userPlayer().get("id"))) {
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
