public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        oDisplay.instance().clearAndRefresh();
        nServer.clearBots();
        cVars.put("botbehavior", "");
        if(!mapPath.contains(sVars.get("datapath")))
            mapPath = eUtils.getPath(mapPath);
        gMap.load(mapPath);
        eManager.setScene();
        oDisplay.instance().createPanels();
        cScripts.setupGame();
        return "";
    }
}
