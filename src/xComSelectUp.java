public class xComSelectUp extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            uiInterface.blockMouseUI = true;
            uiMenus.prevItem();
        }
        else
            return xCon.ex("playerup");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            return xCon.ex("-playerup");
        return fullCommand;
    }
}
