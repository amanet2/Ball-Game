public class xComSelectUp extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
            cVars.putInt("blockmouseui", 1);
            uiMenus.prevItem();
        }
        return fullCommand;
    }
}
