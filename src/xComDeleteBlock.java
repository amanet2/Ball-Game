public class xComDeleteBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(sSettings.IS_SERVER)
                deleteBlockDelegate(id, cServerLogic.scene);
            if(sSettings.IS_CLIENT)
                deleteBlockDelegate(id, cClientLogic.scene);
        }
        return "usage: deleteblock <id>";
    }

    private void deleteBlockDelegate(String id, gScene scene) {
        if(scene.getThingMap("THING_BLOCK").containsKey(id)) {
            gBlock blockToDelete = (gBlock) scene.getThingMap("THING_BLOCK").get(id);
            String type = blockToDelete.get("type");
            scene.getThingMap("THING_BLOCK").remove(id);
            scene.getThingMap(type).remove(id);
        }
    }
}
