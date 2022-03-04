import java.awt.*;

public class dFlares {
    public static void drawSceneFlares(Graphics2D g2, gScene scene){
        if(sSettings.vfxenableflares) {
            for(String id : scene.getThingMap("THING_FLARE").keySet()) {
                gFlare f = (gFlare) scene.getThingMap("THING_FLARE").get(id);
                drawFlare(g2, f);
            }
        }
    }

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

    public static void drawFlare(Graphics2D g2, gFlare f) {
        if(sSettings.vfxenableflares) {
            int x = f.getInt("coordx");
            int y = f.getInt("coordy");
            int w = f.getInt("dimw");
            int h = f.getInt("dimh");
            int[] c1 = new int[]{f.getInt("r1"), f.getInt("g1"), f.getInt("b1"), f.getInt("a1")};
            int[] c2 = new int[]{f.getInt("r2"), f.getInt("g2"), f.getInt("b2"), f.getInt("a2")};
            drawFlare(g2,x,y,w,h,f.getInt("mode"),c1,c2);
        }
    }
}
