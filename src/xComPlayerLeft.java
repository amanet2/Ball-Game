public class xComPlayerLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            cClientLogic.getUserPlayer().putInt("mov2", 1);
        else if(sSettings.show_mapmaker_ui)
            gCamera.move(2);
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            cClientLogic.getUserPlayer().putInt("mov2", 0);
        else
            gCamera.stopMove(2);
        return fullCommand;
    }
}
