import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class iMouseWheel implements MouseWheelListener {
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            if(sVars.isOne("inconsole")) {
                if(xCon.instance().linesToShowStart > 0) {
                    xCon.instance().linesToShowStart--;
                }
            }
            else if(sSettings.show_mapmaker_ui) {
                xCon.ex("-e_nextthing");
            }
            else {
                if(cVars.isZero("gamespawnarmed") && !cVars.isZero("currentweapon")) {
//                    cScripts.changeWeapon(0);
                    xCon.ex("dropweapon");
                }
                else
                    cScripts.changeWeapon(cVars.getInt("currentweapon") > 0
                        ? cVars.getInt("currentweapon") - 1 : gWeapons.weaponSelection().size() - 1);
            }
        } else {
            if(sVars.isOne("inconsole")) {
                if(xCon.instance().linesToShowStart <
                    xCon.instance().stringLines.size() - xCon.instance().linesToShow) {
                    xCon.instance().linesToShowStart++;
                }
            }
            else if(sSettings.show_mapmaker_ui) {
                xCon.ex("e_nextthing");
            }
            else {
                if(cVars.isZero("gamespawnarmed") && !cVars.isZero("currentweapon")) {
//                    cScripts.changeWeapon(0);
                    xCon.ex("dropweapon");
                }
                else
                    cScripts.changeWeapon(cVars.getInt("currentweapon") < gWeapons.weaponSelection().size() - 1
                            ? cVars.getInt("currentweapon") + 1 : 0);
            }
        }
    }
}
