import java.awt.Image;

public class gTile extends gThing {
    Image[] sprites;
    Image spriteT;
    Image spriteTW;

    public gTile(int x, int y, int w, int h, int nh, int nmh, int mh, int smh, int sh, int lw, int rw, String tt,
                 String mt, String bt, int bl, int sp) {
        super();
        put("type", "THING_TILE");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("canspawn", sp);
        putInt("brightness", bl);
        putInt("dim0h", nh);
        putInt("dim1h", nmh);
        putInt("dim2h", mh);
        putInt("dim3h", smh);
        putInt("dim4h", sh);
        putInt("dim5w", lw);
        putInt("dim6w", rw);
        put("sprite0", tt);
        put("sprite1", mt);
        put("sprite2", bt);
        put("occupied", "0");

        for(int j = 0; j < 5; j++) {
            if(getInt(String.format("dim%dh", j)) != 0)
                put(String.format("dim%dw", j), get("dimw"));
            else
                put(String.format("dim%dw", j), "0");
        }
        for(int j = 5; j < 7; j++) {
            if(getInt(String.format("dim%dw", j)) != 0)
                putInt(String.format("dim%dh", j),
                        getInt("dimh")-nh-smh-sh);
            else
                putInt(String.format("dim%dh", j), 0);
        }
        
        sprites = new Image[] {
            gTextures.getScaledImage(tt, getInt("dim0w"), getInt("dim0h")),
            gTextures.getScaledImage(mt, getInt("dim1w"), getInt("dim1h")),
            gTextures.getScaledImage(bt, getInt("dim2w"), getInt("dim2h")),
            gTextures.getScaledImage(tt, getInt("dim3w"), getInt("dim3h")),
            gTextures.getScaledImage(mt, getInt("dim4w"), getInt("dim4h")),
            gTextures.getScaledImage(tt, getInt("dim5w"), getInt("dim5h")),
            gTextures.getScaledImage(tt, getInt("dim6w"), getInt("dim6h"))
        };

        if(getInt("dim6w") < 0) {
            spriteT = gTextures.getScaledImage(tt, getInt("dimw"), getInt("dimh"));
            spriteTW = gTextures.getScaledImage(mt, getInt("dimw"), getInt("dimh") + 150);
        }
    }
}
