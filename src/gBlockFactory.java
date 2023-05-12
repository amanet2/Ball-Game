import javax.imageio.ImageIO;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class gBlockFactory {
    // TODO: this factory is used by both client and server maybe textures should be elsewhere?
    TexturePaint floorTexture;
    TexturePaint wallTexture;
    TexturePaint topTexture;

    public gBlockFactory() {
        try {
            floorTexture = new TexturePaint(ImageIO.read(new File(eManager.getPath("tiles/floor.png"))),
                    new Rectangle2D.Double(0,0,1200, 1200));
            wallTexture = new TexturePaint(ImageIO.read(new File(eManager.getPath("tiles/wall.png"))),
                    new Rectangle2D.Double(0,0, 300, 300));
            topTexture = new TexturePaint(ImageIO.read(new File(eManager.getPath("tiles/top.png"))),
                    new Rectangle2D.Double(0,0, 300, 300));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
