public class gItemFlagBlue extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && !cVars.isVal("blueflagmasterid", player.get("id"))) {
            cVars.put("blueflagmasterid", player.get("id"));
            nServer.instance().addNetCmd("echo " + player.get("name") + " has the blue flag!");
            nServer.instance().addNetCmd("deleteitem " + get("id"));
        }
    }
    public gItemFlagBlue(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_FLAGBLUE");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/flag_blue.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
