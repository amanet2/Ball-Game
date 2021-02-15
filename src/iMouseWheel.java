import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class iMouseWheel implements MouseWheelListener {
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            if(sVars.isOne("inconsole")) {
                if(xCon.linesToShowStart > 0) {
                    xCon.linesToShowStart--;
                }
            }
            else if(sSettings.show_mapmaker_ui) {
                xCon.ex("-e_nextthing");
            }
            else {
                xCon.ex("dropweapon");
            }
        }
        else {
            if(sVars.isOne("inconsole")) {
                if(xCon.linesToShowStart <
                    xCon.stringLines.size() - xCon.linesToShow) {
                    xCon.linesToShowStart++;
                }
            }
            else if(sSettings.show_mapmaker_ui) {
                xCon.ex("e_nextthing");
            }
            else {
                xCon.ex("dropweapon");
            }
        }
    }
}
