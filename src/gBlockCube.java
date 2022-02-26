import java.awt.*;

public class gBlockCube extends gBlock{
    Color colorWall;

    public gBlockCube(int x, int y, int w, int h, int toph, int wallh) {
        super(x, y, w, h);
        put("type", "BLOCK_CUBE");
        putInt("toph", toph); //"toph" will be dimh - wallh
        putInt("wallh", wallh); //"toph" will be dimh - wallh
    }
}
