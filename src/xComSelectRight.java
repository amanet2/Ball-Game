public class xComSelectRight extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[
                    uiMenus.selectedMenu].selectedItem].doItem();
            xCon.ex("playsound sounds/splash.wav");
        }
        else
            return xCon.ex("playerright");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sSettings.show_mapmaker_ui)
            return xCon.ex("-playerright");
        return fullCommand;
    }
}
