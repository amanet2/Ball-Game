public class xComGoBackUI extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay)
            xCon.ex("pause");
        else {
            if(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0) {
                if(!cClientLogic.maploaded) {
                    //offline mode do this
                    uiMenus.selectedMenu = uiMenus.MENU_QUIT;
                    xCon.ex("playsound sounds/splash.wav");
                }
                else
                    xCon.ex("pause");
            }
            else {
                uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
                xCon.ex("playsound sounds/splash.wav");
            }
        }
        return fullCommand;
    }
}
