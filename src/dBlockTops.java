import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dBlockTops {
    public static void drawBlockTopCube(Graphics2D g2, gThing block) {
        g2.setPaint(gBlockFactory.instance().topTexture);
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getInt("toph"));
        dFonts.setFontColor(g2, "clrw_topcolor");
        if(block.contains("wallh") && block.getInt("wallh") < 300)
            dFonts.setFontColor(g2, "clrw_topcolordark");
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getInt("toph"));
        dBlockTopsShading.drawBlockTopShadingCube(g2, block);
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        HashMap<String, gThing> squareMap = uiEditorMenus.previewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gThing block = squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockWalls.drawBlockWallCubePreview(g2, block);
            }
        }
        for(String tag : squareMap.keySet()) {
            gThing block = squareMap.get(tag);
            if(block.contains("toph")) {
                dBlockTops.drawBlockTopCubePreview(g2, block);
            }
        }
    }

    public static void drawBlockTopCubePreview(Graphics2D g2, gThing block) {
        dFonts.setFontColor(g2, "clrw_topcolorpreview");
        g2.fillRect(
                eUtils.scaleInt(block.getX()/4),
                eUtils.scaleInt(block.getY()/4),
                eUtils.scaleInt(block.getWidth()/4),
                eUtils.scaleInt(block.getInt("toph")/4)
        );
    }
}
