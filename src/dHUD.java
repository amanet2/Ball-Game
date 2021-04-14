import java.awt.*;
import java.awt.event.KeyEvent;

public class dHUD {
    public static void drawHUD(Graphics g) {
        gPlayer userPlayer = cGameLogic.userPlayer();
        if(userPlayer == null)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(dFonts.hudStroke);
        //camera indicator
        if(!cVars.isInt("cammode", gCamera.MODE_TRACKING)) {
            dFonts.setFontNormal(g);
            for(Integer i : xCon.instance().pressBinds.keySet()) {
                if(xCon.instance().pressBinds.get(i).contains("centercamera")) {
                    dFonts.drawCenteredString(g,">>PRESS ["+ KeyEvent.getKeyText(i)+"] TO RE-CENTER CAMERA<<",
                            sSettings.width / 2, 2*sSettings.height/3);
                }
            }
        }
        //flashlight
//        if(cVars.isOne("flashlight")) {
//            g.setColor(new Color(0,0,0,255));
//            g.fillRect(sSettings.width/64,56*sSettings.height/64,sSettings.width/3,
//                    sSettings.height/64);
//            g.setColor(new Color(210,160,0,255));
//            g.fillRect(sSettings.width/64,56*sSettings.height/64,
//                    sSettings.width/3,sSettings.height/64);
//            g.setColor(new Color(150,130,0,255));
//            g.drawRect(sSettings.width/64,56*sSettings.height/64,sSettings.width/3,
//                    sSettings.height/64);
//            g.setColor(new Color(200,200,200,255));
//            g.drawString("FLASHLIGHT", sSettings.width/62,56*sSettings.height/64);
//        }
        //health
        g.setColor(new Color(0,0,0,255));
        g.fillRect(sSettings.width/64,60 * sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        g.setColor(new Color(220,0,30,255));
        g.fillRect(sSettings.width/64,60 * sSettings.height/64,
                sSettings.width/3*userPlayer.getInt("stockhp")/cVars.getInt("maxstockhp"),
                sSettings.height/64);
        g.setColor(new Color(150,0,0,255));
        g.drawRect(sSettings.width/64,60 * sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        if(userPlayer.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                userPlayer.getLong("hprechargetime") + cVars.getInt("delayhp")
                        >= System.currentTimeMillis()) {
            double reloadratio = (double)(
                    userPlayer.getLong("hprechargetime") + cVars.getInt("delayhp")
                            - System.currentTimeMillis())/cVars.getInt("delayhp");
            g.setColor(new Color(255,60,150,100));
            g.fillRect(sSettings.width/64,60 * sSettings.height/64,
                    (int)(sSettings.width/3*reloadratio),
                    sSettings.height/64);
        }
        //ammo
        if(userPlayer.get("weapon") != null && gWeapons.fromCode(userPlayer.getInt("weapon")) != null) {
            g.setColor(new Color(0, 0, 0, 255));
            g.fillRect(sSettings.width / 64, 62 * sSettings.height / 64, sSettings.width / 3,
                    sSettings.height / 64);
            g.setColor(new Color(30, 50, 220, 255));
            if (gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo > 0)
                g.fillRect(sSettings.width / 64, 62 * sSettings.height / 64,
                        sSettings.width / 3 * cVars.getInt("weaponstock" + userPlayer.get("weapon"))
                                / gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo,
                        sSettings.height / 64);
//        }
            g2.setColor(Color.BLACK);
            for (int j = 0; j < gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo; j++) {
                g2.drawRect(sSettings.width / 64
                                + (j * ((sSettings.width / 3) / gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo)),
                        62 * sSettings.height / 64,
                        ((sSettings.width / 3) / gWeapons.fromCode(userPlayer.getInt("weapon")).maxAmmo),
                        sSettings.height / 64);
            }
            g.setColor(new Color(0, 0, 150, 255));
            g.drawRect(sSettings.width / 64, 62 * sSettings.height / 64, sSettings.width / 3,
                    sSettings.height / 64);
        }
        //sprint
//        g.setColor(new Color(0,0,0,255));
//        g.fillRect(sSettings.width/64,62*sSettings.height/64,sSettings.width/3,
//                sSettings.height/64);
//        g.setColor(new Color(20,170,80,255));
//        g.fillRect(sSettings.width/64,62*sSettings.height/64,
//                sSettings.width/3*cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed"),sSettings.height/64);
//        g.setColor(new Color(0,100,25,255));
//        g.drawRect(sSettings.width/64,62*sSettings.height/64,sSettings.width/3,
//                sSettings.height/64);
    }
}
