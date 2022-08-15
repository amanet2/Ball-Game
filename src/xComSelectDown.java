public class xComSelectDown extends xCom {
    public String doCommand(String fullCommand) {
        uiMenus.nextItem();
        return fullCommand;
    }
}
