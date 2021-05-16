public class xComDeleteItemClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(cClientLogic.scene.getThingMap("THING_ITEM").containsKey(id)) {
                gItem itemToDelete = (gItem) cClientLogic.scene.getThingMap("THING_ITEM").get(id);
                String type = itemToDelete.get("type");
                cClientLogic.scene.getThingMap("THING_ITEM").remove(id);
                cClientLogic.scene.getThingMap(type).remove(id);
            }
        }
        return "usage: deleteitem <id>";
    }
}
