import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;


public class dHUD {
    public static void drawHUD(Graphics g) {
        gPlayer userPlayer = cClientLogic.getUserPlayer();
        if(userPlayer == null)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(dFonts.hudStroke);
//        health
        g.setColor(Color.black);
        g.fillRect(sSettings.width/64,59 * sSettings.height/64,sSettings.width/8,
                sSettings.height/64);
        g.setColor(gColors.instance().getColorFromName("clrp_" + cClientLogic.playerColor));
        g.fillRect(sSettings.width/64,59 * sSettings.height/64,
                sSettings.width/8*(int)userPlayer.getDouble("stockhp")/cClientLogic.maxhp,
                sSettings.height/64);
        dFonts.setFontNormal(g);
        //score
        nStateMap clStateMap = nClient.instance().clientStateMap;
        if(clStateMap.contains(uiInterface.uuid) && clStateMap.get(uiInterface.uuid).contains("score")) {
            g.setColor(gColors.instance().getColorFromName("clrp_" + cClientLogic.playerColor));
            g.drawString("$ "+ clStateMap.get(uiInterface.uuid).get("score").split(":")[1],
                    sSettings.width / 64, 58*sSettings.height/64);
        }
        dFonts.setFontColor(g, "clrf_normaldark");
        g.drawString(cClientLogic.playerName, sSettings.width / 64, 62*sSettings.height/64);
        g.setColor(gColors.instance().getColorFromName("clrp_" + cClientLogic.playerColor));
        g.fillRect(sSettings.width/128, 28*sSettings.height/32, sSettings.width/256, 3*sSettings.height/32);
        // other players on server
        dFonts.setFontSmall(g);
        int ctr = 1;
        for (String id : clStateMap.keys()) {
            if(id.equals(uiInterface.uuid))
                continue;
            dFonts.setFontColor(g, "clrf_normaldark");
            String color = "blue";
            if(clStateMap.get(id).contains("color"))
                color = clStateMap.get(id).get("color");
            g.setColor(gColors.instance().getColorFromName("clrp_" + color));
            String score = "0:0";
            if(clStateMap.get(id).contains("score"))
                score = clStateMap.get(id).get("score");
            g.drawString("$ " + score.split(":")[1],
                    sSettings.width / 64, 55 * sSettings.height / 64 - (ctr * (sSettings.height / 32)));
            dFonts.setFontColor(g, "clrf_normaldark");
            g.drawString(clStateMap.get(id).get("name"), sSettings.width / 64,
                    56 * sSettings.height / 64 - (ctr * (sSettings.height / 32)));
            g.setColor(gColors.instance().getColorFromName("clrp_" + color));
            g.fillRect(sSettings.width / 128, 54 * sSettings.height / 64 - (ctr * (sSettings.height / 32)),
                    sSettings.width / 256, sSettings.height / 32);
            ctr++;
        }
    }
}
