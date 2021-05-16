public class xComStartServer extends xCom {
    public String doCommand(String fullCommand) {
        nServer.instance().clientArgsMap.put("server", nServer.instance().getNetVars());
        nServer.instance().start();
        sSettings.IS_SERVER = true;
        return "new game started";
    }
}
