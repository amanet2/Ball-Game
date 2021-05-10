public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        oDisplay.instance().clearAndRefresh();
//        nServer.instance().clearBots();
        cVars.put("botbehavior", "");
        //load the most basic blank map
        eManager.currentMap = new gMap();
        gClientLogic.setUserPlayer(null);
        cVars.putLong("starttime", System.currentTimeMillis());
        oDisplay.instance().createPanels();
        return "";
    }
}
