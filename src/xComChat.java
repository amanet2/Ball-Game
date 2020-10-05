public class xComChat extends xCom {
    public String doCommand(String fullCommand) {
        gMessages.enteringMessage = true;
        return fullCommand;
    }
}
