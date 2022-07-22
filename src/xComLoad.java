public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        //load the most basic blank map
        gTextures.clear();
        cClientLogic.gamemode = cGameLogic.DEATHMATCH;
        cServerLogic.scene = new gScene();
        cServerLogic.starttime = System.currentTimeMillis();
        return "";
    }
}
