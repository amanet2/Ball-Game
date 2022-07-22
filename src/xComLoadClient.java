public class xComLoadClient extends xCom {
    public String doCommand(String fullCommand) {
        //load the most basic blank map
        gTextures.clear();
        cClientLogic.gamemode = cGameLogic.DEATHMATCH;
        cClientLogic.scene = new gScene();
        return "";
    }
}
