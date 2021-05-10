public class gItemTeleporterBlue extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            gThing exit = null;
            for(String id : eManager.currentMap.scene.getThingMap("ITEM_TELEPORTER_BLUE").keySet()) {
                gThing teleporter = eManager.currentMap.scene.getThingMap("ITEM_TELEPORTER_BLUE").get(id);
                if(!isVal("id", teleporter.get("id")))
                    exit = teleporter;
            }
            if(exit != null) {
                player.put("inteleporter", "1");
                player.put("coordx", exit.get("coordx"));
                player.put("coordy", exit.get("coordy"));
                nServer.instance().addNetCmd(player.get("id"), "userplayer coordx " + exit.get("coordx")
                        + ";userplayer coordy " + exit.get("coordy"));
            }
        }
        else
            player.put("inteleporter", "1");
    }
    public gItemTeleporterBlue(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_TELEPORTER_BLUE");
        sprite = gTextures.getScaledImage(eUtils.getPath("misc/teleporter_blue.png"),
                getInt("dimw"), getInt("dimh"));
    }
}
