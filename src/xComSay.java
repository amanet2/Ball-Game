public class xComSay extends xCom {
    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String msg = fullCommand.substring(fullCommand.indexOf(" ")+1);
            msg = sVars.get("playername") + ": " + msg;
            String echoString = "echo " + msg;
            if(sSettings.IS_SERVER) {
                String testmsg = msg.substring(msg.indexOf(':')+2);
                nServer.instance().checkMessageForSpecialSound(testmsg);
                nServer.instance().checkMessageForVoteToSkip(testmsg);
                nServer.instance().addNetCmd(echoString);
            }
            else {
                switch (sSettings.NET_MODE) {
                    case sSettings.NET_CLIENT:
                        nClient.instance().addSendMsg(msg);
                        break;
                    case sSettings.NET_OFFLINE:
                        xCon.ex(echoString);
                }
            }
            gMessages.msgInProgress = "";
        }
        return fullCommand;
    }
}
