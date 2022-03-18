import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class iMouseWheel implements MouseWheelListener {
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            if(uiInterface.inconsole)
                xCon.instance().linesToShowStart = Math.max(0, xCon.instance().linesToShowStart-1);
        }
        else {
            if(uiInterface.inconsole)
                xCon.instance().linesToShowStart = Math.min(xCon.instance().stringLines.size()
                        - xCon.instance().linesToShow, xCon.instance().linesToShowStart + 1);
        }
    }
}
