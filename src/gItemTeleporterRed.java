public class gItemTeleporterRed extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            player.put("inteleporter", "1");
            nServer.instance().addNetCmd("echo " + player.get("name") + " entered the red teleporter");
        }
    }
    public gItemTeleporterRed(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_TELEPORTER_RED");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/teleporter_red.png"), getInt("dimw"), getInt("dimh"));
    }
}
