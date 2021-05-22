public class xComSelectLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sVars.isOne("showmapmakerui")) {
            if(!(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0))
                uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
        }
        else
            xCon.ex("playerleft");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sVars.isOne("showmapmakerui"))
            xCon.ex("-playerleft");
        return fullCommand;
    }
}
