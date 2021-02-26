public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.net_server) {
            xCon.ex("clearbots");
            nServer.addExcludingNetCmd("server", "disconnect");
        }
        cVars.put("disconnecting", "1");
        return fullCommand;
    }
}
