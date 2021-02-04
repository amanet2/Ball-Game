import java.awt.*;
import java.util.ArrayList;

public class dFlares {
    public static void drawSceneFlares(Graphics2D g2){
        ArrayList<gFlare> flareMap = eManager.currentMap.scene.flares();
        for(gFlare f : flareMap) {
            drawFlare(g2, f);
        }
    }

    public static void drawFlare(Graphics2D g2, int x, int y, int w, int h, int mode,
                                 int[] rgba1, int[] rgba2) {
        if(sVars.isOne("vfxenableflares")) {
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

    public static void drawFlare(Graphics2D g2, gFlare f) {
        if(sVars.isOne("vfxenableflares")) {
            float r = (float) Math.random();
            if((f.getInt("flicker") > 0 && r < 1.0-((double)f.getInt("flicker")*0.01)) || f.getInt("flicker") < 1) {
                int x = eUtils.scaleInt(f.getInt("coordx")-cVars.getInt("camx"));
                int y = eUtils.scaleInt(f.getInt("coordy")-cVars.getInt("camy"));
                int w = eUtils.scaleInt(f.getInt("dimw"));
                int h = eUtils.scaleInt(f.getInt("dimh"));
                int[] c1 = new int[]{f.getInt("r1"), f.getInt("g1"), f.getInt("b1"), f.getInt("a1")};
                int[] c2 = new int[]{f.getInt("r2"), f.getInt("g2"), f.getInt("b2"), f.getInt("a2")};
                drawFlare(g2,x,y,w,h,f.getInt("mode"),c1,c2);
            }
        }
    }
}
