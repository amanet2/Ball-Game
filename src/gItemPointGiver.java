public class gItemPointGiver extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        xCon.ex(String.format("exec items/pointgiver %s %s", player.get("id"), get("id")));
    }

    public gItemPointGiver(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_POINTGIVER");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/light1.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
