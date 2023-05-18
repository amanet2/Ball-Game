import java.awt.*;

public class dDrawPayload {
    Image[] sprites;
    int[] spriteDims;
    boolean shadow;
    boolean isPlayerShading;
    int holdingWeapon;

    public dDrawPayload(Image[] sprites, int[] dims, boolean shadow, boolean isPlayerShading) {
        this.sprites = sprites;
        this.spriteDims = dims;
        this.shadow = shadow;
        this.isPlayerShading = isPlayerShading;
        holdingWeapon = 0;
    }
}
