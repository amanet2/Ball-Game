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
//        if(xCon.getInt("displaymode") == oDisplay.displaymode_fullscreen) {
//            int screenw = (int) oDisplay.instance().getScreenHardwareDimensions()[0];
//            int screenh = (int) oDisplay.instance().getScreenHardwareDimensions()[1];
//            if(getWidth() !=screenw || getHeight() != screenh) {
//                setBounds(0,0,screenw,screenh);
//            }
////            g2.translate(screenw/2, screenh/2);
//            g2.scale(screenw/sSettings.width,screenh/sSettings.height);
////            g2.translate(-screenw/2, -screenh/2);
//        }
        if(panelLevel == 1)
            vFrameFactory.drawFrame(g2, 1);
        else
            vFrameFactory.drawFrame(g2, 0);
        g2.dispose();
        //image queue
//        if(vFrameFactory.instance().frameImageQueue.size() > 0) {
//            Image[] frames = new Image[]{vFrameFactory.showImage == 1 ? vFrameFactory.bi : vFrameFactory.bib,
//                                         vFrameFactory.showImage == 1 ? vFrameFactory.bi2 : vFrameFactory.bib2};
//            for (int i = 0; i < frames.length; i++) {
//                if(xCon.getInt("displaymode") == oDisplay.displaymode_fullscreen)
//                    g.drawImage(frames[i], 0, 0, (int) oDisplay.instance().getScreenHardwareDimensions()[0],
//                            (int) oDisplay.instance().getScreenHardwareDimensions()[1],null);
//                else
//                    g.drawImage(frames[i], 0, 0, null);
//            }
//        }
        g.dispose();
//        oDisplay.instance().frame.getBufferStrategy().show();
    }

    public dPanel() {
        super();
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(true);
    }
}
