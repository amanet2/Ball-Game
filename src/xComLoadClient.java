public class xComLoadClient extends xCom {
    public String doCommand(String fullCommand) {
        //load the most basic blank map
        gTextures.clear();
        xCon.ex("cv_gamemode 0");
        cClientLogic.scene = new gScene();
        return "";
    }
}
