public class xComSelectUp extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            gCamera.move(0);
        else {
            cVars.putInt("hidemouseui", 1);
            uiMenus.prevItem();
        }
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        if(sVars.isZero("inconsole"))
            gCamera.stopMove(0);
        return fullCommand;
    }
}
