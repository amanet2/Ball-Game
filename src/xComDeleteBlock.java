public class xComDeleteBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(sSettings.IS_SERVER) {
                if(cServerLogic.scene.getThingMap("THING_BLOCK").containsKey(id)) {
                    gBlock blockToDelete = (gBlock) cServerLogic.scene.getThingMap("THING_BLOCK").get(id);
                    String type = blockToDelete.get("type");
                    cServerLogic.scene.getThingMap("THING_BLOCK").remove(id);
                    if(cServerLogic.scene.getThingMap(type).containsKey(id))
                        cServerLogic.scene.getThingMap(type).remove(id);
                }
            }
            if(sSettings.IS_CLIENT) {
                if(cClientLogic.scene.getThingMap("THING_BLOCK").containsKey(id)) {
                    gBlock blockToDelete = (gBlock) cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
                    String type = blockToDelete.get("type");
                    cClientLogic.scene.getThingMap("THING_BLOCK").remove(id);
                    if(cClientLogic.scene.getThingMap(type).containsKey(id))
                        cClientLogic.scene.getThingMap(type).remove(id);
                }
            }
        }
        return "usage: deleteblock <id>";
    }
}
