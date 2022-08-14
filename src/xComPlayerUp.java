public class xComPlayerUp extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() == null && sSettings.show_mapmaker_ui)
            xCon.ex("setcam mode 0;setcam mov0 1");
        return fullCommand;
    }
}