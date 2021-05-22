public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            cClientLogic.getUserPlayer().putInt("mov1", 1);
        else if(sSettings.show_mapmaker_ui)
            gCamera.move(1);
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            cClientLogic.getUserPlayer().putInt("mov1", 0);
        else
            gCamera.stopMove(1);
        return fullCommand;
    }
}
