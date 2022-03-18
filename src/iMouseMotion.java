import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class iMouseMotion implements MouseMotionListener {

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(uiInterface.blockMouseUI)
            uiInterface.blockMouseUI = false;
    }
}