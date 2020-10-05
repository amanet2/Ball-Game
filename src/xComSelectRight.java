public class xComSelectRight extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            gCamera.move(3);
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            gCamera.stopMove(3);
        else {
            uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[
                uiMenus.selectedMenu].selectedItem].doItem();
            xCon.ex("playsound sounds/splash.wav");
        }
        return fullCommand;
    }
}
