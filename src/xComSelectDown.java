public class xComSelectDown extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            uiInterface.blockMouseUI = true;
            uiMenus.nextItem();
        }
        return fullCommand;
    }
}
