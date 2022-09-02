import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Point;
import java.awt.Color;

public class dFlares {
    public static void drawFlareFromColor(Graphics2D g2, int x, int y, int w, int h, int mode, Color c1, Color c2) {
        RadialGradientPaint df = new RadialGradientPaint(new Point(x + w/2, y + h/2),
                mode == 1 ? Math.max(w/2, h/2) : Math.min(w/2, h/2),
                new float[]{0f, 1f}, new Color[]{c1, c2}
        );
        g2.setPaint(df);
        g2.fillRect(x, y, w, h);
    }

    public static void drawFlare(Graphics2D g2, int x, int y, int w, int h, int mode,
                                 int[] rgba1, int[] rgba2) {
        RadialGradientPaint df = new RadialGradientPaint(new Point(x + w/2, y + h/2),
                mode == 1 ? Math.max(w/2, h/2) : Math.min(w/2, h/2),
                new float[]{0f, 1f}, new Color[]{
                    new Color(rgba1[0],rgba1[1],rgba1[2],rgba1[3]),
                    new Color(rgba2[0],rgba2[1],rgba2[2],rgba2[3])
                }
        );
        g2.setPaint(df);
        g2.fillRect(x, y, w, h);
    }
}
