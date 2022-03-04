public class gItemShotgun extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && !cVars.get("shotgunmasterids").contains(player.get("id"))) {
            String masters = cVars.get("shotgunmasterids");
            masters += (player.get("id") + "-");
            cVars.put("shotgunmasterids", masters);
            nServer.instance().addExcludingNetCmd("server", "echo " + player.get("name")
                    + " has the shotgun");
            nServer.instance().addNetCmd("deleteitem " + get("id"));
        }
    }

    public gItemShotgun(int x, int y) {
        super(x, y, 200, 100);
        put("type", "ITEM_SHOTGUN");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/shotgun.png"), getInt("dimw"), getInt("dimh"));
    }
}
