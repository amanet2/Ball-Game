public class xComDeleteBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(eManager.currentMap.scene.getThingMap("THING_BLOCK").containsKey(id)) {
                gBlock blockToDelete = (gBlock) eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
                String type = blockToDelete.get("type");
                eManager.currentMap.scene.getThingMap("THING_BLOCK").remove(id);
                if(eManager.currentMap.scene.getThingMap(type).containsKey(id)) {
                    eManager.currentMap.scene.getThingMap(type).remove(id);
                    return "removed " + type + " id: " + id;
                }
                else
                    return "no " + type + " found for id: " + id;
            }
        }
        return "usage: deleteblock <id>";
    }
}
