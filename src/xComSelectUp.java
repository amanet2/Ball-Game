public class xComSelectUp extends xCom {
    public String doCommand(String fullCommand) {
        uiMenus.prevItem();
        return fullCommand;
    }
}
