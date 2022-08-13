public class xComPlayerLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            xCon.ex(String.format("cl_setthing THING_PLAYER %s mov2 1", uiInterface.uuid));
        else if(sSettings.show_mapmaker_ui)
            xCon.ex("setcam mode 0;setcam mov2 1");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            xCon.ex(String.format("cl_setthing THING_PLAYER %s mov2 0", uiInterface.uuid));
        else
            xCon.ex("setcam mov2 0");

        return fullCommand;
    }
}
