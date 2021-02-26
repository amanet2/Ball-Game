public class xComClearBots extends xCom {
    public String doCommand(String fullCommand) {
        for(String id : nServer.clientIds) {
            if(id.contains("bot"))
                nServer.quitClientIds.add(id);
            eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").clear();
        }
        return "bot removed";
    }
}
