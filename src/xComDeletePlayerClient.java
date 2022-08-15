public class xComDeletePlayerClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(id.equals(uiInterface.uuid))
                cClientVars.instance().put("userplayerid", "null");
            cClientLogic.scene.getThingMap("THING_PLAYER").remove(id);
            if(id.contains("bot"))
                cClientLogic.scene.getThingMap("THING_BOTPLAYER").remove(id);
        }
        return "usage: deleteplayer <id>";
    }
}
