import java.awt.*;
import java.awt.event.KeyEvent;

public class dHUD {
    public static void drawHUD(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(eUtils.scaleInt(10)));
        //camera indicator
        if(cVars.getInt("cammode") == gCamera.MODE_FREE) {
            dFonts.setFontNormal(g);
            for(Integer i : xCon.instance().pressBinds.keySet()) {
                if(xCon.instance().pressBinds.get(i).contains("centercamera")) {
                    dFonts.drawCenteredString(g,">>PRESS ["+ KeyEvent.getKeyText(i)+"] TO RE-CENTER CAMERA<<",
                            sSettings.width / 2, 2*sSettings.height/3);
                }
            }
        }
        //flashlight
        if(cVars.isOne("flashlight")) {
            g.setColor(new Color(0,0,0,255));
            g.fillRect(sSettings.width/64,56*sSettings.height/64,sSettings.width/3,
                    sSettings.height/64);
            g.setColor(new Color(210,160,0,255));
            g.fillRect(sSettings.width/64,56*sSettings.height/64,
                    sSettings.width/3,sSettings.height/64);
            g.setColor(new Color(150,130,0,255));
            g.drawRect(sSettings.width/64,56*sSettings.height/64,sSettings.width/3,
                    sSettings.height/64);
            g.setColor(new Color(200,200,200,255));
            g.drawString("FLASHLIGHT", sSettings.width/62,56*sSettings.height/64);
        }
        //health
        g.setColor(new Color(0,0,0,255));
        g.fillRect(sSettings.width/64,58*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        g.setColor(new Color(220,0,30,255));
        g.fillRect(sSettings.width/64,58*sSettings.height/64,
                sSettings.width/3*cVars.getInt("stockhp")/cVars.getInt("maxstockhp"),sSettings.height/64);
        g.setColor(new Color(150,0,0,255));
        g.drawRect(sSettings.width/64,58*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        if(cVars.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                cVars.getLong("hprechargetime") + cVars.getInt("delayhp")
                        >= System.currentTimeMillis()) {
            double reloadratio = (double)(
                    cVars.getLong("hprechargetime") + cVars.getInt("delayhp")
                            - System.currentTimeMillis())/cVars.getInt("delayhp");
            g.setColor(new Color(255,60,150,100));
            g.fillRect(sSettings.width/64,58*sSettings.height/64,
                    (int)(sSettings.width/3*reloadratio),
                    sSettings.height/64);
        }
        g.setColor(new Color(200,200,200,255));
        g.drawString("HEALTH", sSettings.width/62,58*sSettings.height/64);
        //ammo
        g.setColor(new Color(0,0,0,255));
        g.fillRect(sSettings.width/64,60*sSettings.height/64, sSettings.width/3,
                sSettings.height/64);
        if(cScripts.isReloading()) {
            double reloadratio = (double)(
                    cVars.getLong("weapontime"+cVars.get("currentweapon"))+cVars.getInt("delayweap")
                            - System.currentTimeMillis())/cVars.getInt("delayweap");
            g.setColor(new Color(255,255,255,100));
            g.fillRect(sSettings.width/64,60*sSettings.height/64,
                    (int)(sSettings.width/3-(sSettings.width/3*reloadratio)),
                    sSettings.height/64);
        }
        else {
            g.setColor(new Color(30,50,220,255));
            if(gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo > 0)
                g.fillRect(sSettings.width/64,60*sSettings.height/64,
                        sSettings.width/3*cVars.getInt("weaponstock"+cVars.get("currentweapon"))
                                /gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo,
                        sSettings.height/64);
        }
        g2.setColor(Color.BLACK);
        for(int j = 0; j < gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo;j++) {
            g2.drawRect(
                    sSettings.width/64
                            + (j*((sSettings.width/3)/gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo)),
                    60*sSettings.height/64,
                    ((sSettings.width/3)/gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo),
                    sSettings.height/64);
        }
        g.setColor(new Color(0,0,150,255));
        g.drawRect(sSettings.width/64,60*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        g.setColor(new Color(200,200,200,255));
        g.drawString(cScripts.isReloading() ? "-- RELOADING --"
                        : cVars.getInt("currentweapon") != gWeapons.type.NONE.code()
                        && cVars.getInt("currentweapon") != gWeapons.type.GLOVES.code()
                        ? (gWeapons.fromCode(cVars.getInt("currentweapon")).name.toUpperCase()
                        + " ["+cVars.getInt("weaponstock"+cVars.getInt("currentweapon"))+ "]")
                        : gWeapons.fromCode(cVars.getInt("currentweapon")).name.toUpperCase(),
                sSettings.width/62,60*sSettings.height/64);
        //sprint
        g.setColor(new Color(0,0,0,255));
        g.fillRect(sSettings.width/64,62*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        g.setColor(new Color(20,170,80,255));
        g.fillRect(sSettings.width/64,62*sSettings.height/64,
                sSettings.width/3*cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed"),sSettings.height/64);
        g.setColor(new Color(0,100,25,255));
        g.drawRect(sSettings.width/64,62*sSettings.height/64,sSettings.width/3,
                sSettings.height/64);
        g.setColor(new Color(200,200,200,255));
        g.drawString("BOOST", sSettings.width/62,62*sSettings.height/64);
    }
}
