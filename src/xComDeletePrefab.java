public class xComDeletePrefab extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String did = toks[1];
            for(String id : cServerLogic.scene.getThingMapIds("THING_BLOCK")) {
                gBlock block = (gBlock) cServerLogic.scene.getThingMap("THING_BLOCK").get(id);
                if(!block.isVal("prefabid", did))
                    continue;
                String type = block.get("type");
                cServerLogic.scene.getThingMap("THING_BLOCK").remove(id);
                cServerLogic.scene.getThingMap(type).remove(id);
            }
        }
        return "usage: deleteprefab <id>";
    }
}
