public class gItemLandmine extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
    }

    public gItemLandmine(int x, int y) {
        super("ITEM_LANDMINE", x, y, 300, 300,
                gTextures.getGScaledImage(eUtils.getPath("misc/forbidden.png"), 300, 300));
        put("script", "exec items/landmine");
    }
}
