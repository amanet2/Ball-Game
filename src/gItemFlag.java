public class gItemFlag extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        nServer.instance().clientArgsMap.get("server").put("flagmasterid", player.get("id"));
        xCon.ex("setvar pnam " + nServer.instance().clientArgsMap.get(player.get("id")).get("name"));
        xCon.ex("setvar pcol " + nServer.instance().clientArgsMap.get(player.get("id")).get("color"));
        xCon.ex("exec items/flag");
    }

    public gItemFlag(int x, int y) {
        super(x, y, 200, 300);
        put("type", "ITEM_FLAG");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
