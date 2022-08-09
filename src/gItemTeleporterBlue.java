public class gItemTeleporterBlue extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            gThing exit = null;
            for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").keySet()) {
                gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").get(id);
                if(!isVal("id", teleporter.get("id")))
                    exit = teleporter;
            }
            if(exit != null) {
                player.put("inteleporter", "1");
                xCon.ex(String.format("exec items/teleporter %s %s %s", player.get("id"),
                        exit.getInt("coordx") + exit.getInt("dimw")/2 - player.getInt("dimw")/2,
                        exit.getInt("coordy") + exit.getInt("dimh")/2 - player.getInt("dimh")/2)
                );
            }
        }
        else
            player.put("inteleporter", "1");
    }
    public gItemTeleporterBlue(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_TELEPORTER_BLUE");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_blue.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
