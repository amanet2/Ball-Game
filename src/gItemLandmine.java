public class gItemLandmine extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        xCon.ex("exec scripts/landmine " + get("id"));
        nServer.instance().addNetCmd("deleteitem " + get("id"));
    }

    public gItemLandmine(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_LANDMINE");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/forbidden.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
