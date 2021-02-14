public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        oDisplay.instance().clearAndRefresh();
        nServer.clearBots();
        cVars.put("botbehavior", "");
        eManager.currentMap = mapPath.length() > 0 ? new gMap(
            mapPath.contains(sVars.get("datapath")) ? mapPath : eUtils.getPath(mapPath)) : new gMap();
        eManager.setScene();
        oDisplay.instance().createPanels();
        cScripts.setupGame();
//        if(sSettings.net_server) {
//            cVars.put("sendcmd", fullCommand);
//        }
        return "";
    }
}
