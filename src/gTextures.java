import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class gTextures {
    private static Map<String, ImageIcon> base_sprites = new HashMap<>();
    private static HashMap<String, Image> scaled_sprites = new HashMap<>();

    static String[] selection_top = new String[]{
        "objects/scenery/sceneryBlock_top.png",
        "objects/scenery/sceneryBlock2_top.png",
        "objects/scenery/sceneryBlock3_top.png",
        "objects/scenery/sceneryBlock5_top.png",
        "none"
    };
    static String[] selection_wall = new String[]{
        "objects/scenery/sceneryBlock_bottom.png",
        "objects/scenery/sceneryBlock2_bottom.png",
        "objects/scenery/sceneryBlock3_bottom.png",
        "objects/scenery/sceneryBlock5_bottom.png",
        "none"
    };
    static String[] selection_floor = new String[]{
        "objects/wood/more_rough_wood_6.jpg",
        "objects/metal/brushed_metal1.jpg",
        "objects/floor/dungeons_and_flagons3.jpg",
        "misc/grass.png",
        "misc/water.png",
        "misc/sand.png",
        "misc/spookybg.jpg",
        "misc/horror.jpg",
        "none"
    };

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
        for (gTile t : eManager.currentMap.scene.tiles()) {
            t.sprites = new Image[]{
                    gTextures.getScaledImage(t.get("sprite0"), t.getInt("dim0w"), t.getInt("dim0h")),
                    gTextures.getScaledImage(t.get("sprite1"), t.getInt("dim1w"), t.getInt("dim1h")),
                    gTextures.getScaledImage(t.get("sprite2"), t.getInt("dim2w"), t.getInt("dim2h")),
                    gTextures.getScaledImage(t.get("sprite0"), t.getInt("dim3w"),  t.getInt("dim3h")),
                    gTextures.getScaledImage(t.get("sprite1"), t.getInt("dim4w"), t.getInt("dim4h")),
                    gTextures.getScaledImage(t.get("sprite0"), t.getInt("dim5w"), t.getInt("dim5h")),
                    gTextures.getScaledImage(t.get("sprite0"), t.getInt("dim6w"), t.getInt("dim6h"))
            };
        }
        for (gPlayer t : eManager.currentMap.scene.players()) {
            t.sprite = getScaledImage(t.get("pathsprite"), t.getInt("dimw"), t.getInt("dimh"));
        }
        for (gProp t : eManager.currentMap.scene.props()) {
            t.sprite = getScaledImage(t.get("sprite"), t.getInt("dimw"), t.getInt("dimh"));
        }
    }
}
