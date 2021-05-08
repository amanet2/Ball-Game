public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        oDisplay.instance().clearAndRefresh();
        nServer.instance().clearBots();
        cVars.put("botbehavior", "");
        //load the most basic blank map
        eManager.currentMap = new gMap();
        cGameLogic.setUserPlayer(null);
        if(sSettings.show_mapmaker_ui)
            cVars.put("maploaded", "1");
        cVars.putLong("starttime", System.currentTimeMillis());
        oDisplay.instance().createPanels();
        return "";
    }
}
