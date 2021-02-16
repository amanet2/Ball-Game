public class xComSay extends xCom {
    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String msg = fullCommand.substring(fullCommand.indexOf(" ")+1);
            msg = sVars.get("playername") + ": " + msg;
            if(sSettings.net_server) {
                String testmsg = msg.substring(msg.indexOf(':')+2);
                nServer.checkMessageForSpecialSound(testmsg);
                nServer.checkMessageForVoteToSkip(testmsg);
                String echoString = "echo " + msg;
                xCon.ex(echoString);
                nServer.addSendCmd(echoString);
            }
            else if(sSettings.net_client)
                nClient.addSendMsg(msg);
        }
        return fullCommand;
    }
}
