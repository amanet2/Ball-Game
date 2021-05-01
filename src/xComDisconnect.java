public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {
        switch (sSettings.NET_MODE) {
            case sSettings.NET_SERVER:
                nServer.instance().addExcludingNetCmd("server", "disconnect");
            case sSettings.NET_CLIENT:
                nClient.instance().disconnect(); //leaves it to the server timeout to remove player
        }
        return fullCommand;
    }
}
