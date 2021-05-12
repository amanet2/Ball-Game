public class xComRemovePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(id);
            if(id.equals(uiInterface.uuid))
                cClientLogic.setUserPlayer(null);
            if(id.contains("bot"))
                eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").remove(id);
            nServer.instance().addExcludingNetCmd("server,"+uiInterface.uuid, fullCommand);
        }
        return "usage: removeplayer <id>";
    }
}
