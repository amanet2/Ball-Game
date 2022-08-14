public class gItemTeleporterBlue extends gItem {
    public void activateItem(gPlayer player) {
        super.activateItem(player);
        if((int)player.getDouble("stockhp") < 1 || player.isOne("inteleporter"))
            return;
        gThing exit = null;
        for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").keySet()) {
            gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").get(id);
            if(!isVal("id", teleporter.get("id"))) {
                exit = teleporter;
                break;
            }
        }
        if(exit == null)
            return;
        xCon.ex(String.format("exec items/teleporter %s %s",
                exit.getInt("coordx") + exit.getInt("dimw")/2 - player.getInt("dimw")/2,
                exit.getInt("coordy") + exit.getInt("dimh")/2 - player.getInt("dimh")/2)
        );
    }
    public gItemTeleporterBlue(int x, int y) {
        super("ITEM_TELEPORTER_BLUE", x, y, 300, 300,
                gTextures.getGScaledImage(eUtils.getPath("misc/teleporter_blue.png"),
                300, 300));
    }
}
