import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class iMouse implements MouseListener {
    static boolean holdingMouseLeft;
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e))
            xCon.ex("mouseleft");
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e))
            xCon.ex("-mouseleft");
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}