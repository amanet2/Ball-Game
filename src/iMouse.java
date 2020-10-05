import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class iMouse implements MouseListener {
    static boolean holdingMouseRight;
    static boolean holdingMouseLeft;
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            doLeftMouseCommand();
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            doRightMouseCommand();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            stopLeftMouseCommand();
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            stopRightMouseCommand();
        }
    }

    void doLeftMouseCommand() {
        xCon.ex("mouseleft");
    }

    void doRightMouseCommand() {
        xCon.ex("mouseright");
    }

    void stopLeftMouseCommand() {
        xCon.ex("-mouseleft");
    }

    void stopRightMouseCommand() {
        xCon.ex("-mouseright");
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}