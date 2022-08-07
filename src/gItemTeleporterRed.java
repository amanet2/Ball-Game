public class gItemTeleporterRed extends gItem {
    public void activateItem(gPlayer player) {
        if(player.getInt("stockhp") > 0 && player.isZero("inteleporter")) {
            gThing exit = null;
            for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_RED").keySet()) {
                gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_RED").get(id);
                if(!isVal("id", teleporter.get("id")))
                    exit = teleporter;
            }
            if(exit != null) {
                int nx = exit.getInt("coordx") + exit.getInt("dimw")/2 - player.getInt("dimw")/2;
                int ny = exit.getInt("coordy") + exit.getInt("dimh")/2 - player.getInt("dimh")/2;
                String pid = player.get("id");
                player.put("inteleporter", "1");
                xCon.ex("exec scripts/teleporter " + nx + " " + ny);
                nServer.instance().addNetCmd(pid, "cl_setthing THING_PLAYER " + pid + " coordx " + nx
                        + ";cl_setthing THING_PLAYER " + pid + " coordy " + ny);
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
