public class xComChangeMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 2)
            return "usage: changemap <path_to_mapfile>";
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        cServerLogic.changeMap(mapPath, gTime.gameTime);
        return "";
    }
}
