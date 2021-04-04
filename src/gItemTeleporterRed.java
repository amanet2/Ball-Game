public class gItemTeleporterRed extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            gThing exit = null;
            for(String id : eManager.currentMap.scene.getThingMap("ITEM_TELEPORTER_RED").keySet()) {
                gThing teleporter = eManager.currentMap.scene.getThingMap("ITEM_TELEPORTER_RED").get(id);
                if(!isVal("id", teleporter.get("id")))
                    exit = teleporter;
            }
            if(exit != null) {
                player.put("inteleporter", "1");
                player.put("coordx", exit.get("coordx"));
                player.put("coordy", exit.get("coordy"));
                nServer.instance().addNetCmd("echo " + player.get("name") + " entered the red teleporter");
            }
        }
        else
            player.put("inteleporter", "1");
    }
    public gItemTeleporterRed(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_TELEPORTER_RED");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/teleporter_red.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
