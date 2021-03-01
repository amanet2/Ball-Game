public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.net_server) {
            xCon.ex("clearbots");
            nServer.instance().addExcludingNetCmd("server", "disconnect");
        }
        if(sSettings.net_client) {
            nClient.instance().disconnect();
        }
        cVars.put("disconnecting", "1");
        return fullCommand;
    }
}
