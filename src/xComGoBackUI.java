public class xComGoBackUI extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay)
            cScripts.doPause();
        else {
            if(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0)
                cScripts.doPause();
            else {
                uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
                xCon.ex("playsound sounds/splash.wav");
            }
        }
        return fullCommand;
    }
}
