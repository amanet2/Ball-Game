public class xComDumpThingMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            if(eManager.currentMap.scene.objectMaps.containsKey(thing_title)) {
                return eManager.currentMap.scene.objectMaps.get(thing_title).toString();
            }
        }
        return "usage: dumpthingmap <thing_title>";
    }
}