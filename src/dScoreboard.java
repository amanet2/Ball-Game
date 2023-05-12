import java.awt.*;

public class dScoreboard {
    private static final String dividerString = "_______________________";
    public static void showScoreBoard(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColor(g, "clrf_highlight");
        dFonts.drawCenteredString(g, sSettings.clientGameModeTitle.toUpperCase() + ": " + sSettings.clientGameModeText,
                sSettings.width/2, 2*sSettings.height/30);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawString(clStateMap.keys().size() + " players",
                sSettings.width/3,5*sSettings.height/30);
        g.drawString("                           Wins",sSettings.width/3,5*sSettings.height/30);
        g.drawString("                                       Score",sSettings.width/3,5*sSettings.height/30);
        g.drawString(dividerString,
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
            if(id.equals(uiInterface.uuid))
                isMe = true;
            if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]) < prevscore)
                place++;
            String hudName = place + "." + clStateMap.get(id).get("name");
            int coordx = sSettings.width/3;
            int coordy = 7 * sSettings.height / 30 + ctr * sSettings.height / 30;
            int height = sSettings.height / 30;
            String ck = clStateMap.get(id).get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            dFonts.drawPlayerNameScoreboard(g, hudName, coordx, coordy, color);
            if(cClientLogic.getPlayerById(id) != null) {
                Image sprite = gTextures.getGScaledImage(eManager.getPath(String.format("animations/player_%s/a03.png", ck)), sSettings.height / 30, sSettings.height / 30);
                g.drawImage(sprite, coordx - sSettings.height / 30, coordy - height, null);
            }
            g.setColor(color);
            if(isMe)
                g.drawRect(coordx, coordy - height,
                        dFonts.getStringWidth(g, dividerString), dFonts.getStringHeight(g, hudName));
            g.drawString("                           "
                            + clStateMap.get(id).get("score").split(":")[0], sSettings.width/3, coordy);
            g.drawString("                                       "
                            + clStateMap.get(id).get("score").split(":")[1], sSettings.width/3, coordy);
            dFonts.setFontColor(g, "clrf_normal");
            if(isMe)
                isMe = false;
            ctr++;
            prevscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]);
        }
    }
}
