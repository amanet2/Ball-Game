import java.awt.*;

public class dDrawPayload {
    Image[] sprites;
    int[] spriteDims;
    boolean shadow;
    boolean isPlayerShading;
    boolean isFlare;
    String flareColorString;
    Color flareColor;
    boolean isWeapon;
    double fv;

    public dDrawPayload(Image[] sprites, int[] dims, boolean shadow, boolean isPlayerShading, boolean isFlare, String flareColorString, Color flareColor, boolean isWeapon, double fv) {
        this.sprites = sprites;
        this.spriteDims = dims;
        this.shadow = shadow;
        this.isPlayerShading = isPlayerShading;
        this.isFlare = isFlare;
        this.flareColorString = flareColorString;
        this.flareColor = flareColor;
        this.isWeapon = isWeapon;
        this.fv = fv;
    }
}
