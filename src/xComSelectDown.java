public class xComSelectDown extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            gCamera.move(1);
        else
            uiMenus.nextItem();
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        if(sVars.isZero("inconsole"))
            gCamera.stopMove(1);
        return fullCommand;
    }
}
