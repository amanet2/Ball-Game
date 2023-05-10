import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class iMouseWheel implements MouseWheelListener {
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            if(uiInterface.inconsole)
                xMain.shellLogic.console.linesToShowStart = Math.max(0, xMain.shellLogic.console.linesToShowStart-1);
        }
        else {
            if(uiInterface.inconsole)
                xMain.shellLogic.console.linesToShowStart = Math.min(xMain.shellLogic.console.stringLines.size()
                        - xMain.shellLogic.console.linesToShow, xMain.shellLogic.console.linesToShowStart + 1);
        }
    }
}
