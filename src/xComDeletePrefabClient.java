public class xComDeletePrefabClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String did = toks[1];
            for(String id : cClientLogic.scene.getThingMapIds("THING_BLOCK")) {
                gBlock block = (gBlock) cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
                if(!block.isVal("prefabid", did))
                    continue;
                String type = block.get("type");
                cClientLogic.scene.getThingMap("THING_BLOCK").remove(id);
                cClientLogic.scene.getThingMap(type).remove(id);
            }
        }
        return "usage: deleteblock <id>";
    }
}
