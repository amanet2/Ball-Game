public class xComActivateUI extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sVars.isOne("showmapmakerui")) {
            uiMenus.menuSelection[uiMenus.selectedMenu].items[
                uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem].doItem();
            xCon.ex("playsound sounds/tap.wav");
        }
        return "activateUI";
    }
}
