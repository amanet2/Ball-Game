public class xComDeleteItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(sSettings.IS_SERVER)
                deleteItemDelegate(id, cServerLogic.scene);
            if(sSettings.IS_CLIENT)
                deleteItemDelegate(id, cClientLogic.scene);
        }
        return "usage: deleteblock <id>";
    }

    private void deleteItemDelegate(String id, gScene scene) {
        if(scene.getThingMap("THING_ITEM").containsKey(id)) {
            gItem itemToDelete = (gItem) scene.getThingMap("THING_ITEM").get(id);
            String type = itemToDelete.get("type");
            scene.getThingMap("THING_ITEM").remove(id);
            scene.getThingMap(type).remove(id);
        }
    }
}
