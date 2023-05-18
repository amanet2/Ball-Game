import java.awt.*;

public class dDrawPayload {
    Image[] sprites;
    int[] spriteDims;
    boolean shadow;

    public dDrawPayload(Image[] sprites, int[] dims, boolean shadow) {
        this.sprites = sprites;
        this.spriteDims = dims;
        this.shadow = shadow;
    }
}
