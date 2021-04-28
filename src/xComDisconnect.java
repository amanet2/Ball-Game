public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {

        if(sSettings.net_server) {
            nServer.instance().addExcludingNetCmd("server", "disconnect");
            cVars.put("disconnecting", "1");
        }
        if(sSettings.net_client) {
            nClient.instance().disconnect(); //leaves it to the server timeout to remove player
        }
        return fullCommand;
    }
}
