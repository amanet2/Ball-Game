public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        //load the most basic blank map
        gTextures.clear();
        cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
        cServerLogic.scene = new gScene();
        cVars.putLong("starttime", System.currentTimeMillis());
        return "";
    }
}
