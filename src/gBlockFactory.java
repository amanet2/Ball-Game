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
    BufferedImage floorImage;
    TexturePaint floorTexture;
    BufferedImage wallImage;
    TexturePaint wallTexture;
    BufferedImage topImage;
    TexturePaint topTexture;
    Color topColor;
    Color topColorDark;
    Color topColorPreview;
    Color wallColorPreview;
    Color floorColorPreview;

    private gBlockFactory() {
        blockLoadMap = new HashMap<>();
        blockLoadMap.put("BLOCK_CUBE", new gDoableBlockReturnCube());
        blockLoadMap.put("BLOCK_FLOOR", new gDoableBlockReturnFloor());
        blockLoadMap.put("BLOCK_COLLISION", new gDoableBlockReturnCollision());
        try {
            floorImage = ImageIO.read(new File(eUtils.getPath("tiles/floor.png")));
            wallImage = ImageIO.read(new File(eUtils.getPath("tiles/wall.png")));
            topImage = ImageIO.read(new File(eUtils.getPath("tiles/top.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        topColor = new Color(
                125,
                125,
                125,
                100
        );
        topColorDark = new Color(
                0,
                0,
                0,
                100
        );
        topColorPreview = new Color(190,190,180,255);
        wallColorPreview = new Color(120, 120, 200, 255);
        floorColorPreview = new Color(100,100,60,255);
        floorTexture = new TexturePaint(floorImage, new Rectangle2D.Double(0,0,
                eUtils.scaleInt(1200),eUtils.scaleInt(1200)));
        wallTexture = new TexturePaint(wallImage, new Rectangle2D.Double(0,0,
                eUtils.scaleInt(300),eUtils.scaleInt(300)));
        topTexture = new TexturePaint(topImage, new Rectangle2D.Double(0,0,
                eUtils.scaleInt(300),eUtils.scaleInt(300)));
    }

    public static gBlockFactory instance() {
        if(instance == null)
            instance = new gBlockFactory();
        return instance;
    }
}
