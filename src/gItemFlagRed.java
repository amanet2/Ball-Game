public class gItemFlagRed extends gItem {
    public gItemFlagRed(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_FLAGRED");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_red.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
