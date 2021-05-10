public class xComChangeMap extends xCom {
    public String doCommand(String fullCommand) {
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        cServerLogic.changeMap(mapPath);
        cServerLogic.resetGameState();
        return "";
    }
}
