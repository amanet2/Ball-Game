public class xComPlayerUp extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            xCon.ex(String.format("cl_setthing THING_PLAYER %s mov0 1", uiInterface.uuid));
        else if(sSettings.show_mapmaker_ui)
            xCon.ex("setcam mode 0;setcam mov0 1");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            xCon.ex(String.format("cl_setthing THING_PLAYER %s mov0 0", uiInterface.uuid));
        else
            xCon.ex("setcam mov0 0");

        return fullCommand;
    }
}