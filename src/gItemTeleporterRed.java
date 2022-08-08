public class gItemTeleporterRed extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            gThing exit = null;
            for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_RED").keySet()) {
                gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_RED").get(id);
                if(!isVal("id", teleporter.get("id")))
                    exit = teleporter;
            }
            if(exit != null) {
                player.put("inteleporter", "1");
                xCon.ex(String.format("setvar teleid %s;setvar telex %d;setvar teley %d;exec %s", exit.get("id"),
                        exit.getInt("coordx") + exit.getInt("dimw")/2 - player.getInt("dimw")/2,
                        exit.getInt("coordy") + exit.getInt("dimh")/2 - player.getInt("dimh")/2,
                        "items/teleporter"));
            }
        }
        else
            player.put("inteleporter", "1");
    }
    public gItemTeleporterRed(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_TELEPORTER_RED");
        sprite = gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_red.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
