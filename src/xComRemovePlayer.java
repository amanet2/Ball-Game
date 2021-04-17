public class xComRemovePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(id);
            if(id.equals(uiInterface.uuid)) {
                cGameLogic.setUserPlayer(null);
            }
            if(id.contains("bot"))
                eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").remove(id);
            if(sSettings.net_server) {
                nServer.instance().addExcludingNetCmd("server", fullCommand);
            }
        }
        return "usage: removeplayer <id>";
    }
}
