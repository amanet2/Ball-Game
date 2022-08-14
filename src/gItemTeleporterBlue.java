public class gItemTeleporterBlue extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
    }
    public gItemTeleporterBlue(int x, int y) {
        super("ITEM_TELEPORTER_BLUE", x, y, 300, 300,
                gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_blue.png"), 300, 300));
        put("script", "exec items/teleporterblue");
    }
}
