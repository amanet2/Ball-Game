import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class gTextures {
    private static Map<String, ImageIcon> base_sprites = new HashMap<>();
    private static HashMap<String, Image> scaled_sprites = new HashMap<>();

    public static Image getScaledImage(String s, int w, int h) {
        if(w < 1 || h < 1 || s.replace(sVars.get("datapath")+"/", "").equalsIgnoreCase("none"))
            return null;
        String rk = String.format("%s%d%d",s,w,h);
        if(base_sprites.get(s) == null) {
            File f = new File(s);
            if(!f.exists() || f.isDirectory()) {
                base_sprites.put(s, null);
                scaled_sprites.put(rk, null);
                return null;
            }
            else
                base_sprites.put(s, new ImageIcon(s));
        }
        if(scaled_sprites.get(rk) == null) {
            scaled_sprites.put(rk,
                    base_sprites.get(s).getImage().getScaledInstance(
                            eUtils.scaleInt(w), eUtils.scaleInt(h), Image.SCALE_FAST));
        }
        return scaled_sprites.get(rk);
    }

    static void clear() {
        base_sprites.clear();
        scaled_sprites.clear();
    }

    public static void refreshObjectSprites() {
        scaled_sprites.clear();
        for(String id : gScene.getPlayerIds()) {
            gPlayer p = gScene.getPlayerById(id);
            p.sprite = getScaledImage(p.get("pathsprite"), p.getInt("dimw"), p.getInt("dimh"));
        }
    }
}
