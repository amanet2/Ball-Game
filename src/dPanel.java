import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * JPanel
 * displays image on the screen
 */
public class dPanel extends JPanel {
    int panelLevel = 0;
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        removeAll();
        //drawpanel
        Graphics2D g2 = (Graphics2D) g;
        if(panelLevel == 1)
            vFrameFactory.drawFrame(g2, 1);
        else
            vFrameFactory.drawFrame(g2, 0);
        g2.dispose();
        g.dispose();
    }

    public dPanel() {
        super();
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(true);
    }
}
