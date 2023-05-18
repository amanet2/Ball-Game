import java.awt.*;

public class dDrawPayload {
    Image[] sprites;
    int[] spriteDims;
    boolean shadow;
    int holdingWeapon;

    public dDrawPayload(Image[] sprites, int[] dims, boolean shadow) {
        this.sprites = sprites;
        this.spriteDims = dims;
        this.shadow = shadow;
        holdingWeapon = 0;
    }
}
