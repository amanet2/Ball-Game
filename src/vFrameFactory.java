import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class vFrameFactory {
//    Queue<Image[]> frameImageQueue = new LinkedList<>();
//    static GraphicsConfiguration config =
//            GraphicsEnvironment.getLocalGraphicsEnvironment()
//                    .getDefaultScreenDevice()
//                    .getDefaultConfiguration();
//    static Image bi = config.createCompatibleVolatileImage(sSettings.width, sSettings.height, Transparency.TRANSLUCENT);
//    static Image bi2 = config.createCompatibleVolatileImage(sSettings.width, sSettings.height, Transparency.TRANSLUCENT);
//    static Image bib = config.createCompatibleVolatileImage(sSettings.width, sSettings.height, Transparency.TRANSLUCENT);
//    static Image bib2 = config.createCompatibleVolatileImage(sSettings.width, sSettings.height, Transparency.TRANSLUCENT);
//    static int showImage = 1; //1 or 2
//    private static vFrameFactory instance = null;
//
//    public static vFrameFactory instance() {
//        if(instance == null)
//            instance = new vFrameFactory();
//        return instance;
//    }
//
//    public vFrameFactory() {
//
//    }

    public static void drawFrame(Graphics2D g2, int panelLevel) {
//        Graphics2D g2 = (Graphics2D) bg;
        if (panelLevel == 1) {
            dScreenFX.drawScreenFX(g2);
            dScreenMessages.displayScreenMessages(g2);
        } else {
            g2.translate(sSettings.width / 2, sSettings.height / 2);
            g2.scale(eUtils.zoomLevel, eUtils.zoomLevel);
            g2.translate(-sSettings.width / 2, -sSettings.height / 2);
            dTileFloors.drawFloors(g2);
            dTileWalls.drawWalls(g2);
            dProp.drawProps(g2);
            dPlayer.drawPlayers(g2);
            dTileTops.drawTops(g2);
        }
        g2.dispose();
    }

//    public static Image createFrame(int panelLevel) {
////        BufferedImage bi = new BufferedImage(sSettings.width, sSettings.height, BufferedImage.TYPE_INT_ARGB);
////        Image bi = config.createCompatibleVolatileImage(sSettings.width, sSettings.height, Transparency.TRANSLUCENT);
//        Image process = showImage == 1 && panelLevel == 1 ? bi2 : showImage == 1 && panelLevel == 0 ? bi
//                        : showImage == 0 && panelLevel == 1 ? bib2 : showImage == 0 && panelLevel == 0 ? bib : bib;
////        if(panelLevel == 1) {
////            Graphics bg = bi2.getGraphics();
////            Graphics2D bg2 = (Graphics2D) bg;
////            bg2.setBackground(new Color(0, 0, 0, 0));
////            bg2.clearRect(0, 0, bi2.getWidth(null), bi2.getHeight(null));
////            drawFrame(bg2, panelLevel);
////            bg2.dispose();
////            bg.dispose();
////            return bi2;
////        }
//        Graphics bg = process.getGraphics();
//        Graphics2D bg2 = (Graphics2D) bg;
//        bg2.setBackground(new Color(0, 0, 0, 0));
//        bg2.clearRect(0, 0, process.getWidth(null), process.getHeight(null));
//        drawFrame(bg2, panelLevel);
//        bg2.dispose();
//        bg.dispose();
//        return process;
//    }
}
