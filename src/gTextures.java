import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class gTextures {
    private static final Map<String, ImageIcon> base_gsprites = new HashMap<>();
    private static final HashMap<String, Image> gscaled_sprites = new HashMap<>();

    public static Image getGScaledImage(String s, int w, int h) {
        if(w < 1 || h < 1 || s.replace(sSettings.datapath+"/", "").equalsIgnoreCase("none"))
            return null;
        String rk = String.format("%s%d%d",s,w,h);
        if(base_gsprites.get(s) == null) {
            File f = new File(s);
            if(!f.exists() || f.isDirectory()) {
                base_gsprites.put(s, null);
                gscaled_sprites.put(rk, null);
                return null;
            }
            else
                base_gsprites.put(s, new ImageIcon(s));
        }
        if(gscaled_sprites.get(rk) == null)
            gscaled_sprites.put(rk, base_gsprites.get(s).getImage().getScaledInstance(w, h, Image.SCALE_FAST));
        return gscaled_sprites.get(rk);
    }

    static void clear() {
        base_gsprites.clear();
        gscaled_sprites.clear();
    }

    public static void refreshObjectSprites() {
        gscaled_sprites.clear();
        for(String id : xMain.shellLogic.getPlayerIds()) {
            gPlayer p = xMain.shellLogic.getPlayerById(id);
            p.sprite = getGScaledImage(p.spritePath, p.dims[0], p.dims[1]);
        }
    }
}
