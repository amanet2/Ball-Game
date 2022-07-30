public class gItemPointGiver extends gItem {
    public void activateItem(gPlayer player) {
        String plId = player.get("id");
        xCon.ex(String.format("givepoint %s", plId));
        nServer.instance().addNetCmd("deleteitem " + get("id"));
    }

    public gItemPointGiver(int x, int y) {
        super(x, y, 200, 300);
        put("type", "ITEM_POINTGIVER");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/light1.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
