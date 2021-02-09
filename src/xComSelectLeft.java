public class xComSelectLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            gCamera.move(2);
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            gCamera.stopMove(2);
        else {
            if(!(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0)) {
                uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
            }
        }
        return fullCommand;
    }
}
