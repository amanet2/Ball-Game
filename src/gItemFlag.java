public class gItemFlag extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        nServer.instance().clientArgsMap.get("server").put("flagmasterid", player.get("id"));
        xCon.ex(String.format("exec items/flag %s %s",
                nServer.instance().clientArgsMap.get(player.get("id")).get("name"),
                nServer.instance().clientArgsMap.get(player.get("id")).get("color")));
    }

    public gItemFlag(int x, int y) {
        super("ITEM_FLAG", x, y, 200, 300, gTextures.getGScaledImage(eUtils.getPath("misc/flag.png"),
                200, 300));
    }
}
