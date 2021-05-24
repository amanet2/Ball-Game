public class gItemTeleporterBlue extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            gThing exit = null;
            for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").keySet()) {
                gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").get(id);
                if(!isVal("id", teleporter.get("id")))
                    exit = teleporter;
            }
            if(exit != null) {
                player.put("inteleporter", "1");
                int nx = exit.getInt("coordx") + exit.getInt("dimw")/2 - player.getInt("dimw")/2;
                int ny = exit.getInt("coordy") + exit.getInt("dimh")/2 - player.getInt("dimh")/2;
                player.putInt("coordx", nx);
                player.putInt("coordy", ny);
                nServer.instance().addNetCmd(player.get("id"), "userplayer coordx " + nx
                        + ";userplayer coordy " + ny);
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
