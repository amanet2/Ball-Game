public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        //load the most basic blank map
        gTextures.clear();
        xCon.ex("cv_gamemode " + cGameLogic.DEATHMATCH);
        cServerLogic.scene = new gScene();
        cServerLogic.starttime = gTime.gameTime;
        return "";
    }
}
