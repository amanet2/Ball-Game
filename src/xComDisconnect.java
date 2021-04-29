public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {

        if(sSettings.net_server) {
            nServer.instance().addExcludingNetCmd("server", "disconnect");
            nServer.instance().disconnect();
        }
        if(sSettings.net_client) {
            nClient.instance().disconnect(); //leaves it to the server timeout to remove player
        }
        return fullCommand;
    }
}
