import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class iMouseMotion implements MouseMotionListener {

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(cVars.isOne("blockmouseui")) {
            cVars.put("blockmouseui","0");
        }
    }
}