public class gItemPointGiver extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
    }

    public gItemPointGiver(int x, int y) {
        super("ITEM_POINTGIVER", x, y, 300, 300,
                gTextures.getGScaledImage(eUtils.getPath("misc/light1.png"), 300, 300));
        put("script", "exec items/pointgiver");
    }
}
