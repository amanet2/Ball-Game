public class xComSay extends xCom {
    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String msg = fullCommand.substring(fullCommand.indexOf(" ")+1);
            msg = sVars.get("playername") + ": " + msg;
            String echoString = "echo " + msg;
            switch(sSettings.NET_MODE) {
                case sSettings.NET_SERVER:
                    String testmsg = msg.substring(msg.indexOf(':')+2);
                    nServer.checkMessageForSpecialSound(testmsg);
                    nServer.checkMessageForVoteToSkip(testmsg);
                    xCon.ex(echoString);
                    nServer.addSendCmd(echoString);
                    break;
                case sSettings.NET_CLIENT:
                    nClient.addSendMsg(msg);
                    break;
                default:
                    xCon.ex(echoString);
                    break;
            }
        }
        return fullCommand;
    }
}
