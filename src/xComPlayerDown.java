public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            xCon.ex(String.format("cl_setthing THING_PLAYER %s mov1 1", uiInterface.uuid));
        else if(sSettings.show_mapmaker_ui)
            xCon.ex("setcam mov1 1");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            xCon.ex(String.format("cl_setthing THING_PLAYER %s mov1 0", uiInterface.uuid));
        else
            xCon.ex("setcam mov1 0");

        return fullCommand;
    }
}
