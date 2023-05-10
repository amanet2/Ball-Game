import javax.imageio.ImageIO;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class gBlockFactory {
    HashMap<String, gDoableThingReturn> blockLoadMap;
    // TODO: this factory is used by both client and server maybe textures should be elsewhere?
    TexturePaint floorTexture;
    TexturePaint wallTexture;
    TexturePaint topTexture;

    public gBlockFactory() {
        blockLoadMap = new HashMap<>();
        blockLoadMap.put("BLOCK_CUBE", new gDoableThingReturn(){
            public gThing getThing(String[] args) {
                gBlock block = new gBlock(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                block.put("toph", args[4]);
                block.put("wallh", args[5]);
                block.put("type", "BLOCK_CUBE");
                return block;
            }
        });
        blockLoadMap.put("BLOCK_FLOOR", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gBlock block = new gBlock(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                block.put("type", "BLOCK_FLOOR");
                return block;
            }
        });
        blockLoadMap.put("BLOCK_COLLISION", new gDoableThingReturn() {
            public gThing getThing(String[] args) {
                gBlock block = new gBlock(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                block.put("type", "BLOCK_COLLISION");
                return block;
            }
        });
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
