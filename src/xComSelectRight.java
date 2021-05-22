public class xComSelectRight extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sVars.isOne("showmapmakerui")) {
            uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[
                    uiMenus.selectedMenu].selectedItem].doItem();
            xCon.ex("playsound sounds/splash.wav");
        }
        else
            xCon.ex("playerright");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sVars.isOne("showmapmakerui"))
            xCon.ex("-playerright");
        return fullCommand;
    }
}
