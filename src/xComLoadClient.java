public class xComLoadClient extends xCom {
    public String doCommand(String fullCommand) {
        //load the most basic blank map
        gTextures.clear();
        cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
        cClientLogic.scene = new gScene();
        cVars.putLong("starttime", System.currentTimeMillis());
        return "";
    }
}
