public class xComStartServer extends xCom {
    public String doCommand(String fullCommand) {
        if(!nServer.instance().isAlive())
            nServer.instance().start();
        new eGameServer().start();
        sSettings.IS_SERVER = true;
        return "new game started";
    }
}
