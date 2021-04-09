import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class iMouseWheel implements MouseWheelListener {
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            if(sVars.isOne("inconsole"))
                xCon.linesToShowStart = Math.max(0, xCon.linesToShowStart-1);
        }
        else {
            if(sVars.isOne("inconsole"))
                xCon.linesToShowStart = Math.min(xCon.stringLines.size() - xCon.linesToShow, xCon.linesToShowStart + 1);
        }
    }
}
