import java.awt.*;

public class dTileFloors {
    public static void drawFloors(Graphics2D g2) {
        if(cVars.isOne("maploaded")) {
            dBlockFloors.drawBlockFloors(g2);
        }
    }
}
