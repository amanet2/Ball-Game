public class gItemFlag extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
    }

    public gItemFlag(int x, int y) {
        super("ITEM_FLAG", x, y, 200, 300, gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"),
                200, 300));
        put("script", "exec items/flag");
    }
}
