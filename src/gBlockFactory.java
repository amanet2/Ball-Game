import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class gBlockFactory {
    HashMap<String, gDoableBlockReturn> blockLoadMap;
    private static gBlockFactory instance = null;
    Image floorSprite;
//    Image wallSprite;
    BufferedImage wallImage;
    TexturePaint wallTexture;
    BufferedImage topImage;
    TexturePaint topTexture;

    private gBlockFactory() {
        blockLoadMap = new HashMap<>();
        blockLoadMap.put("BLOCK_CUBE", new gDoableBlockReturnCube());
        blockLoadMap.put("BLOCK_FLOOR", new gDoableBlockReturnFloor());
//        blockLoadMap.put("BLOCK_CORNERUR", new gDoableBlockReturnCornerUR());
//        blockLoadMap.put("BLOCK_CORNERUL", new gDoableBlockReturnCornerUL());
//        blockLoadMap.put("BLOCK_CORNERLR", new gDoableBlockReturnCornerLR());
//        blockLoadMap.put("BLOCK_CORNERLL", new gDoableBlockReturnCornerLL());
        floorSprite = gTextures.getScaledImage(eUtils.getPath("tiles/grass03.png"), 1210, 1210);
//        wallSprite = gTextures.getScaledImage(eUtils.getPath("tiles/wall.png"), 300, 300);
//        wallImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        try {
            wallImage = ImageIO.read(new File(eUtils.getPath("tiles/wall.png")));
            topImage = ImageIO.read(new File(eUtils.getPath("tiles/top.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Graphics2D bGr = wallImage.createGraphics();
//        bGr.drawImage(wallSprite, 0, 0, null);
//        bGr.dispose();
        wallTexture = new TexturePaint(wallImage, new Rectangle2D.Double(0,0,
                eUtils.scaleInt(300),eUtils.scaleInt(300)));
        topTexture = new TexturePaint(topImage, new Rectangle2D.Double(0,0,
                eUtils.scaleInt(1200),eUtils.scaleInt(150)));
    }

    public static gBlockFactory instance() {
        if(instance == null)
            instance = new gBlockFactory();
        return instance;
    }
}
