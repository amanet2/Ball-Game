public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        cVars.put("botbehavior", "");
        //load the most basic blank map
        gTextures.clear();
        cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
        cVars.put("mapname", "new");
        cServerLogic.scene = new gScene();
        cClientLogic.scene = new gScene();
        if(sSettings.IS_SERVER) {
            nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
            nServer.instance().clientArgsMap.get("server").remove("virusids");
        }
        cVars.putLong("starttime", System.currentTimeMillis());
        return "";
    }
}
