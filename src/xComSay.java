public class xComSay extends xCom {
    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String msg = fullCommand.substring(fullCommand.indexOf(" ")+1);
            msg = cClientLogic.playerName + "#"+cClientLogic.playerColor+": " + msg;
            nClient.instance().addSendMsg(msg);
            gMessages.msgInProgress = "";
        }
        return fullCommand;
    }
}
