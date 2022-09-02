import java.awt.Image;

public class gItemFactory {
    static Image flagSprite;
    public static void init() {
        xCon.ex("exec items/itemsdef");
        flagSprite = gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"), 200, 300);
    }
}
