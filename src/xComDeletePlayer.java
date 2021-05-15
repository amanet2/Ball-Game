public class xComDeletePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(sSettings.IS_SERVER) {
                deletePlayerDelegate(id, cServerLogic.scene);
                nServer.instance().addExcludingNetCmd("server,"+uiInterface.uuid, fullCommand);
            }
            if(sSettings.IS_CLIENT)
                deletePlayerDelegate(id, cClientLogic.scene);
        }
        return "usage: deleteplayer <id>";
    }

    private void deletePlayerDelegate(String id, gScene scene) {
        scene.getThingMap("THING_PLAYER").remove(id);
        if(id.contains("bot"))
            scene.getThingMap("THING_BOTPLAYER").remove(id);
    }
}
