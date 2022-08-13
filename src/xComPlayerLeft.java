public class xComPlayerLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() == null && sSettings.show_mapmaker_ui)
            xCon.ex("setcam mode 0;setcam mov2 1");
        return fullCommand;
    }
}
