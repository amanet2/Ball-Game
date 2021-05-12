public class xComDeleteItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(sSettings.IS_SERVER) {
                if(cServerLogic.scene.getThingMap("THING_ITEM").containsKey(id)) {
                    gItem itemToDelete = (gItem) cServerLogic.scene.getThingMap("THING_ITEM").get(id);
                    String type = itemToDelete.get("type");
                    cServerLogic.scene.getThingMap("THING_ITEM").remove(id);
                    if(cServerLogic.scene.getThingMap(type).containsKey(id))
                        cServerLogic.scene.getThingMap(type).remove(id);
                }
            }
            if(sSettings.IS_CLIENT) {
                if(cClientLogic.scene.getThingMap("THING_ITEM").containsKey(id)) {
                    gItem itemToDelete = (gItem) cClientLogic.scene.getThingMap("THING_ITEM").get(id);
                    String type = itemToDelete.get("type");
                    cClientLogic.scene.getThingMap("THING_ITEM").remove(id);
                    if(cClientLogic.scene.getThingMap(type).containsKey(id))
                        cClientLogic.scene.getThingMap(type).remove(id);
                }
            }
        }
        return "usage: deleteblock <id>";
    }
}
