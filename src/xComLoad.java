public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        oDisplay.instance().clearAndRefresh();
        nServer.instance().clearBots();
        cVars.put("botbehavior", "");
        if(mapPath.length() > 0) {
            if (!mapPath.contains(sVars.get("datapath")))
                mapPath = eUtils.getPath(mapPath);
            gMap.load(mapPath);
        }
        else {
            //load the most basic blank map
//            eManager.currentMap = new gMap();
        }
        oDisplay.instance().createPanels();
        return "";
    }
}
