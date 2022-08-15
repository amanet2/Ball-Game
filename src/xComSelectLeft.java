public class xComSelectLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            if(!(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0))
                uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
        }
        else
            return xCon.ex("exec scripts/playerleft");
        return fullCommand;
    }
}
