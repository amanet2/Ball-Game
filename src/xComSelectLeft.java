public class xComSelectLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(!(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0))
            uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
        return fullCommand;
    }
}
