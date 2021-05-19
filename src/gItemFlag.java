public class gItemFlag extends gItem {
    public void activateItem(gPlayer player) {
        nServer.instance().clientArgsMap.get("server").put("flagmasterid", player.get("id"));
        String playername = nServer.instance().clientArgsMap.get(player.get("id")).get("name");
        nServer.instance().addExcludingNetCmd("server",
                "echo " + playername + " has the flag");
        nServer.instance().addNetCmd("deleteitem " + get("id"));
    }

    public gItemFlag(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_FLAG");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/flag.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
