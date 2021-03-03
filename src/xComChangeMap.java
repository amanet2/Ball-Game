public class xComChangeMap extends xCom {
    public String doCommand(String fullCommand) {
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        nServer.instance().changeMap(mapPath);
        return "";
    }
}
