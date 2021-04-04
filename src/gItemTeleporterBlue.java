public class gItemTeleporterBlue extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            player.put("inteleporter", "1");
            nServer.instance().addNetCmd("echo " + player.get("name") + " entered the blue teleporter");
        }
    }
    public gItemTeleporterBlue(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_TELEPORTER_BLUE");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/teleporter_blue.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
