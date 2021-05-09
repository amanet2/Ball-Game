public class xComSay extends xCom {
    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String msg = fullCommand.substring(fullCommand.indexOf(" ")+1);
            msg = sVars.get("playername") + ": " + msg;
            nClient.instance().addSendMsg(msg);
            gMessages.msgInProgress = "";
        }
        return fullCommand;
    }
}
