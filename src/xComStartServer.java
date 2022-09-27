public class xComStartServer extends xCom {
    public String doCommand(String fullCommand) {
        nServer.instance().serverVars = nServer.instance().getNetVars();
        if(!nServer.instance().isAlive())
            nServer.instance().start();
        sSettings.IS_SERVER = true;
        return "new game started";
    }
}
