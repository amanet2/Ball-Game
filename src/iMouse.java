import javax.swing.SwingUtilities;
import java.awt.event.*;

public class iMouse implements MouseListener, MouseMotionListener, MouseWheelListener {
    boolean holdingMouseLeft;

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e))
            xMain.shellLogic.console.ex("mouseleft");
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e))
            xMain.shellLogic.console.ex("-mouseleft");
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(sSettings.hideMouseUI)
            sSettings.hideMouseUI = false;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0 && sSettings.inconsole)
                xMain.shellLogic.console.linesToShowStart = Math.max(0, xMain.shellLogic.console.linesToShowStart-1);
        else if(sSettings.inconsole)
                xMain.shellLogic.console.linesToShowStart = Math.min(Math.max(0, xMain.shellLogic.console.stringLines.size() - xMain.shellLogic.console.linesToShow), xMain.shellLogic.console.linesToShowStart + 1);
    }
}