public class xComDeletePlayerClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            cClientLogic.scene.getThingMap("THING_PLAYER").remove(id);
            if(id.contains("bot"))
                cClientLogic.scene.getThingMap("THING_BOTPLAYER").remove(id);
        }
        return "usage: deleteplayer <id>";
    }
}
