import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dBlockTops {
    public static void drawBlockTopCube(Graphics2D g2, gBlockCube block) {
        gBlockFactory.instance().topTexture = new TexturePaint(gBlockFactory.instance().topImage,
                new Rectangle2D.Double(block.getX(), block.getY(), 300, 300));
        g2.setPaint(gBlockFactory.instance().topTexture);
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getInt("toph"));
        g2.setColor(gBlockFactory.instance().topColor);
        if(block.contains("wallh") && block.getInt("wallh") < 300)
            g2.setColor(gBlockFactory.instance().topColorDark);
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getInt("toph"));
        dBlockTopsShading.drawBlockTopShadingCube(g2, block);
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        HashMap<String, gThing> squareMap = uiEditorMenus.previewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockWalls.drawBlockWallCubePreview(g2, block);
            }
        }
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("toph")) {
                dBlockTops.drawBlockTopCubePreview(g2, block);
            }
        }
    }

    public static void drawBlockTopCubePreview(Graphics2D g2, gBlockCube block) {
        g2.setColor(gBlockFactory.instance().topColorPreview);
        g2.fillRect(
                eUtils.scaleInt(block.getX()/4),
                eUtils.scaleInt(block.getY()/4),
                eUtils.scaleInt(block.getWidth()/4),
                eUtils.scaleInt(block.getInt("toph")/4)
        );
    }
}
