import java.awt.*;

public class dTileWalls {
    public static void drawWalls(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //two passes, first pass corners, second reg walls
        if(cVars.isOne("maploaded")) {
            dBlockWalls.drawBlockWalls(g2);
        }
    }
}
