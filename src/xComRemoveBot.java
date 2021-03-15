public class xComRemoveBot extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            nServer.instance().addQuitClient(id);
            eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").remove(id);
        }
        return "bot removed";
    }
}
