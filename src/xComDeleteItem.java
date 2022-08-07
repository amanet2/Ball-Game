public class xComDeleteItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(cServerLogic.scene.getThingMap("THING_ITEM").containsKey(id)) {
                gItem itemToDelete = (gItem) cServerLogic.scene.getThingMap("THING_ITEM").get(id);
                String type = itemToDelete.get("type");
                cServerLogic.scene.getThingMap("THING_ITEM").remove(id);
                cServerLogic.scene.getThingMap(type).remove(id);
            }
        }
        return "usage: deleteitem <id>";
    }
}
