public class gItemTeleporterRed extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
    }
    public gItemTeleporterRed(int x, int y) {
        super("ITEM_TELEPORTER_RED", x, y, 300, 300,
                gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_red.png"), 300, 300));
        put("script", "exec items/teleporterred");
    }
}
