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
        g.setColor(gColors.getFontColorFromName("normaldark"));
        g.fillRect(sSettings.width/64,59 * sSettings.height/64,
                sSettings.width/8*userPlayer.getInt("stockhp")/cVars.getInt("maxstockhp"),
                sSettings.height/64);
        g.setColor(Color.black);
        g.drawRect(sSettings.width/64,59 * sSettings.height/64,sSettings.width/8,
                sSettings.height/64);
        dFonts.setFontNormal(g);
        g.drawString(Integer.toString(userPlayer.getInt("stockhp")),
                19*sSettings.width/128, 60 * sSettings.height/64);
        //score
        if(nClient.instance().serverArgsMap.containsKey(uiInterface.uuid)
                && nClient.instance().serverArgsMap.get(uiInterface.uuid).containsKey("score")) {
            g.drawString(
                    "$ "+ nClient.instance().serverArgsMap.get(uiInterface.uuid).get("score").split(":")[1],
                    sSettings.width / 64, 58*sSettings.height/64);
        }
        g.drawString(sVars.get("playername"), sSettings.width / 64, 62*sSettings.height/64);
        g.setColor(gColors.getPlayerHudColorFromName(sVars.get("playercolor")));
//        g.fillOval(sSettings.width/64, 30*sSettings.height/32, sSettings.height/32, sSettings.height/32);
        g.fillRect(sSettings.width/128, 28*sSettings.height/32, sSettings.width/256, 3*sSettings.height/32);
//        g.setColor(Color.BLACK);
//        g.drawOval(sSettings.width/64, 30*sSettings.height/32, sSettings.height/32, sSettings.height/32);
//        g.drawRect(sSettings.width/128, 28*sSettings.height/32, sSettings.width/128, 3*sSettings.height/32);
        // other players on server
        dFonts.setFontSmall(g);
        int ctr = 1;
        for (String id : nClient.instance().serverArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid) && !id.equals("server") && nClient.instance().serverArgsMap.get(id).containsKey("score")) {
                g.setColor(gColors.getFontColorFromName("normaldark"));
                g.drawString("$ " + nClient.instance().serverArgsMap.get(id).get("score").split(":")[1],
                        sSettings.width/64, 55*sSettings.height/64-(ctr*(sSettings.height/32)));
                g.drawString(nClient.instance().serverArgsMap.get(id).get("name"), sSettings.width/64,
                        56*sSettings.height/64-(ctr*(sSettings.height/32)));
                String color = nClient.instance().serverArgsMap.get(id).get("color");
                g.setColor(gColors.getPlayerHudColorFromName(color));
                g.fillRect(sSettings.width/128, 54*sSettings.height/64-(ctr*(sSettings.height/32)),
                        sSettings.width/256, sSettings.height/32);
                ctr++;
            }
        }
        // doesn't get here because the rechargetime/delay is only on the server now
//        if(userPlayer.getInt("stockhp") < cVars.getInt("maxstockhp") &&
//                userPlayer.getLong("hprechargetime") + cVars.getInt("delayhp")
//                        >= System.currentTimeMillis()) {
//            double reloadratio = (double)(
//                    userPlayer.getLong("hprechargetime") + cVars.getInt("delayhp")
//                            - System.currentTimeMillis())/cVars.getInt("delayhp");
//            g.setColor(new Color(255,60,150,100));
//            g.fillRect(sSettings.width/64,60 * sSettings.height/64,
//                    (int)(sSettings.width/4*reloadratio),
//                    sSettings.height/64);
//        }
//        //ammo
//        if(userPlayer.get("weapon") != null && gWeapons.fromCode(userPlayer.getInt("weapon")) != null) {
//            g.setColor(new Color(0, 0, 0, 255));
//            g.fillRect(sSettings.width / 64, 62 * sSettings.height / 64, sSettings.width / 3,
//                    sSettings.height / 64);
//            g.setColor(new Color(30, 50, 220, 255));
//            if (gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo > 0)
//                g.fillRect(sSettings.width / 64, 62 * sSettings.height / 64,
//                        sSettings.width / 3 * cVars.getInt("weaponstock" + userPlayer.get("weapon"))
//                                / gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo,
//                        sSettings.height / 64);
//            g2.setColor(Color.BLACK);
//            for (int j = 0; j < gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo; j++) {
//                g2.drawRect(sSettings.width / 64
//                                + (j * ((sSettings.width / 3) / gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo)),
//                        62 * sSettings.height / 64,
//                        ((sSettings.width / 3) / gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo),
//                        sSettings.height / 64);
//            }
//            g.setColor(new Color(0, 0, 150, 255));
//            g.drawRect(sSettings.width / 64, 62 * sSettings.height / 64, sSettings.width / 3,
//                    sSettings.height / 64);
//        }
    }
}
