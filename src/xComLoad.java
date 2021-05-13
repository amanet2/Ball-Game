public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        oDisplay.instance().clearAndRefresh();
        cVars.put("botbehavior", "");
        //load the most basic blank map
        gTextures.clear();
        cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
        cVars.put("mapname", "new");
        if(sSettings.IS_SERVER) {
            cServerLogic.scene = new gScene();
            nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
        }
        if(sSettings.IS_CLIENT) {
            cClientLogic.scene = new gScene();
            cClientLogic.setUserPlayer(null);
            cVars.put("canvoteskip", "");
        }
        cVars.putLong("starttime", System.currentTimeMillis());
        oDisplay.instance().createPanels();
        return "";
    }
}
