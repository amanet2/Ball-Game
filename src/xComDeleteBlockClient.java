public class xComDeleteBlockClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(cClientLogic.scene.getThingMap("THING_BLOCK").containsKey(id)) {
                gBlock blockToDelete = (gBlock) cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
                String type = blockToDelete.get("type");
                cClientLogic.scene.getThingMap("THING_BLOCK").remove(id);
                cClientLogic.scene.getThingMap(type).remove(id);
            }
        }
        return "usage: deleteblock <id>";
    }
}
