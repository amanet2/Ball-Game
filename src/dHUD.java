import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.concurrent.ConcurrentHashMap;


public class dHUD {
    public static void drawHUD(Graphics g) {
        if(!sSettings.IS_CLIENT || !sSettings.clientMapLoaded)
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        nState userState = clStateMap.get(sSettings.uuid);
        if(userState == null)
            return;
        int ctr = 0;
        int hpbarwidth = sSettings.width/8;
        int marginX = sSettings.width/2 - clStateMap.keys().size()*(hpbarwidth/2 + sSettings.width/128);
        for(String id : clStateMap.keys()) {
            nState clState = clStateMap.get(id);
            //healthbar
            g.setColor(Color.black);
            g.fillRect(marginX + ctr*(hpbarwidth + sSettings.width/64)+3,28 * sSettings.height/32+3,hpbarwidth,
                    sSettings.height/24);
            g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
            if(Integer.parseInt(clState.get("hp")) > 0 && xMain.shellLogic.getPlayerById(id) != null)
                g.fillRect(marginX + ctr*(hpbarwidth + sSettings.width/64),28 * sSettings.height/32,
                        hpbarwidth*Integer.parseInt(clState.get("hp"))/ sSettings.clientMaxHP,
                        sSettings.height/24);
            //name
            dFonts.setFontNormal(g);
            g.setColor(Color.BLACK);
            g.drawString(clState.get("name"),
                    marginX + ctr*(hpbarwidth + sSettings.width/64) + 3, 55*sSettings.height/64 + 3);
            g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
            g.drawString(clState.get("name"),
                    marginX + ctr*(hpbarwidth + sSettings.width/64), 55*sSettings.height/64);
            //score
            dFonts.setFontLarge(g);
            if(clState.contains("score")) {
                g.setColor(Color.BLACK);
                g.drawString(clState.get("score").split(":")[1],
                        marginX + ctr*(hpbarwidth + sSettings.width/64) + 3, 63*sSettings.height/64 + 3);
                g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
                g.drawString(clState.get("score").split(":")[1],
                        marginX + ctr*(hpbarwidth + sSettings.width/64), 63*sSettings.height/64);
            }
            ctr++;
        }
    }

    public static void showScoreBoard(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        int spriteRad = sSettings.height/30;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColor(g, "clrf_normal");
        dFonts.drawCenteredString(g, sSettings.clientGameModeTitle + " - " + sSettings.clientGameModeText, sSettings.width/2, 2*spriteRad);
        g.setColor(Color.BLACK);
        g.drawString(clStateMap.keys().size() + " players", sSettings.width/3+3,5*spriteRad+3);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawString(clStateMap.keys().size() + " players", sSettings.width/3,5*spriteRad);
        g.setColor(Color.BLACK);
        g.drawString("                           Wins",sSettings.width/3+3,5*spriteRad+3);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawString("                           Wins",sSettings.width/3,5*spriteRad);
        g.setColor(Color.BLACK);
        g.drawString("                                       Score",sSettings.width/3+3,5*spriteRad+3);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawString("                                       Score",sSettings.width/3,5*spriteRad);
        g.drawString("_______________________", sSettings.width/3, 11*sSettings.height/60);

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
            if(id.equals(sSettings.uuid))
                isMe = true;
            if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]) < prevscore)
                place++;
            String hudName = place + "." + clStateMap.get(id).get("name");
            int coordx = sSettings.width/3;
            int coordy = 7 * sSettings.height / 30 + ctr * sSettings.height / 30;
            int height = sSettings.height / 30;
            String ck = clStateMap.get(id).get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            dFonts.drawScoreBoardPlayerLine(g, hudName, coordx, coordy, color);
            g.setColor(color);
            if(isMe) {
                Polygon myArrow = new Polygon(
                        new int[] {coordx - height, coordx, coordx - height},
                        new int[]{coordy - height, coordy - height/2, coordy},
                        3
                );
                myArrow.translate(3,3);
                g.setColor(Color.BLACK);
                g.fillPolygon(myArrow);
                myArrow.translate(-3,-3);
                g.setColor(color);
                g.fillPolygon(myArrow);
            }
            dFonts.drawScoreBoardPlayerLine(g,
                    "                           " + clStateMap.get(id).get("score").split(":")[0],
                    sSettings.width/3, coordy, color);
            dFonts.drawScoreBoardPlayerLine(g,
                    "                                       " + clStateMap.get(id).get("score").split(":")[1],
                    sSettings.width/3, coordy, color);
            dFonts.setFontColor(g, "clrf_normal");
            if(isMe)
                isMe = false;
            ctr++;
            prevscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]);
        }
    }
}
