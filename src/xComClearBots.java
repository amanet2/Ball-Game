public class xComClearBots extends xCom {
    public String doCommand(String fullCommand) {
        for(String id : nServer.instance().clientIds) {
            if(id.contains("bot"))
                nServer.instance().quitClientIds.add(id);
            eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").clear();
        }
        return "bot removed";
    }
}
