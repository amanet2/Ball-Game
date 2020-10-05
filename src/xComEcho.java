public class xComEcho extends xCom {
    public String doCommand(String fullCommand) {
        String rs = fullCommand.substring(fullCommand.indexOf(" ")+1);
        gMessages.addScreenMessage(rs);
        return rs;
    }
}
