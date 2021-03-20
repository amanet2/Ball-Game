public class xComDeleteBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String type = toks[1];
            String id = toks[2];
            if(eManager.currentMap.scene.getThingMap(type).containsKey(id)) {
                eManager.currentMap.scene.getThingMap(type).remove(id);
                int blockIdCtr = eManager.currentMap.scene.objectIdCtrs.get(type);
                blockIdCtr--;
                if (blockIdCtr < 0)
                    blockIdCtr = 0;
                eManager.currentMap.scene.objectIdCtrs.put(type, blockIdCtr);
                return "removed " + type + " id: " + id;
            }
            else
                return "no " + type + " found for id: " + id;
        }
        return "usage: deleteblock <type> <id>";
    }
}
