public class gItemFlagRed extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && !cVars.isVal("redflagmasterid", player.get("id"))) {
            cVars.put("redflagmasterid", player.get("id"));
            nServer.instance().addNetCmd("echo " + player.get("name") + " has the red flag!");
        }
    }
    public gItemFlagRed(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_FLAGRED");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_red.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
