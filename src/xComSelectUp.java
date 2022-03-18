public class xComSelectUp extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            uiInterface.blockMouseUI = true;
            uiMenus.prevItem();
        }
        else
            xCon.ex("playerup");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            xCon.ex("-playerup");
        return fullCommand;
    }
}
