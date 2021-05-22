public class xComActivateUI extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            uiMenus.menuSelection[uiMenus.selectedMenu].items[
                uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem].doItem();
            xCon.ex("playsound sounds/tap.wav");
        }
        return "activateUI";
    }
}
