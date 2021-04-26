public class gItemFlag extends gItem {
    public void activateItem(gPlayer player) {
        nServer.instance().clientArgsMap.get("server").put("state", player.get("id"));
        nServer.instance().addNetCmd("echo " + player.get("name") + " has the flag!");
        nServer.instance().addNetCmd("deleteitem " + get("id"));
    }
    public gItemFlag(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_FLAG");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/flag.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
