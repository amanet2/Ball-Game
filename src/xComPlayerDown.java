public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() == null && sSettings.show_mapmaker_ui)
            xCon.ex("setcam mode 0;setcam mov1 1");
        return fullCommand;
    }
}
