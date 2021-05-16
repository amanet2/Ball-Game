public class xComLoadClient extends xCom {
    public String doCommand(String fullCommand) {
        cVars.put("botbehavior", "");
        //load the most basic blank map
        gTextures.clear();
        cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
        cVars.put("mapname", "new");
        cClientLogic.scene = new gScene();
        cVars.putLong("starttime", System.currentTimeMillis());
        return "";
    }
}
