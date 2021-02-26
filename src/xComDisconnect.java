public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.net_server) {
            nServer.addExcludingNetCmd("server", "disconnect");
        }
        else if(sSettings.net_client) {
            cVars.put("disconnecting", "1");
        }
        return fullCommand;
    }
}
