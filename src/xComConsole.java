public class xComConsole extends xCom {
    public String doCommand(String fullCommand) {
        uiInterface.inconsole = !uiInterface.inconsole;
        return "console";
    }
}
