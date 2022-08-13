import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

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
                sSettings.width/8*userPlayer.getInt("stockhp")/cClientLogic.maxhp,
                sSettings.height/64);
        dFonts.setFontNormal(g);
        //score
        if(nClient.instance().serverArgsMap.containsKey(uiInterface.uuid)
                && nClient.instance().serverArgsMap.get(uiInterface.uuid).containsKey("score")) {
            g.setColor(gColors.instance().getColorFromName("clrp_" + cClientLogic.playerColor));
            g.drawString(
                    "$ "+ nClient.instance().serverArgsMap.get(uiInterface.uuid).get("score").split(":")[1],
                    sSettings.width / 64, 58*sSettings.height/64);
        }
        dFonts.setFontColor(g, "clrf_normaldark");
        g.drawString(cClientLogic.playerName, sSettings.width / 64, 62*sSettings.height/64);
        g.setColor(gColors.instance().getColorFromName("clrp_" + cClientLogic.playerColor));
        g.fillRect(sSettings.width/128, 28*sSettings.height/32, sSettings.width/256, 3*sSettings.height/32);
        // other players on server
        dFonts.setFontSmall(g);
        int ctr = 1;
        for (String id : nClient.instance().serverArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid) && !id.equals("server") && nClient.instance().serverArgsMap.get(id).containsKey("score")) {
                dFonts.setFontColor(g, "clrf_normaldark");
                String[] requiredFields = {"score", "name", "color"};
                if(nClient.instance().serverArgsMap.containsKey(id)) {
                    for(String s : requiredFields) {
                        if(!nClient.instance().serverArgsMap.get(id).containsKey(s))
                            return;
                    }
                    String color = nClient.instance().serverArgsMap.get(id).get("color");
                    g.setColor(gColors.instance().getColorFromName("clrp_" + color));
                    g.drawString("$ " + nClient.instance().serverArgsMap.get(id).get("score").split(":")[1],
                            sSettings.width / 64, 55 * sSettings.height / 64 - (ctr * (sSettings.height / 32)));
                    dFonts.setFontColor(g, "clrf_normaldark");
                    g.drawString(nClient.instance().serverArgsMap.get(id).get("name"), sSettings.width / 64,
                            56 * sSettings.height / 64 - (ctr * (sSettings.height / 32)));
                    g.setColor(gColors.instance().getColorFromName("clrp_" + color));
                    g.fillRect(sSettings.width / 128, 54 * sSettings.height / 64 - (ctr * (sSettings.height / 32)),
                            sSettings.width / 256, sSettings.height / 32);
                    ctr++;
                }
            }
        }
    }
}
