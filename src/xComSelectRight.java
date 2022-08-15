public class xComSelectRight extends xCom {
    public String doCommand(String fullCommand) {
        uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[
                uiMenus.selectedMenu].selectedItem].doItem();
        return fullCommand;
    }
}
