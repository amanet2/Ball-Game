public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        sSettings.NET_MODE = sSettings.NET_SERVER;
        nServer.instance().clientArgsMap.put("server", nServer.instance().getNetVars());
        cScoreboard.addId(uiInterface.uuid);
        int toplay = eManager.mapSelectionIndex;
        if(toplay < 0) {
            xCon.ex("newgamerandom");
        }
        else {
            xCon.ex(String.format("exec maps/%s", eManager.mapsSelection[toplay]));
        }
        nServer.instance().start();
        return "new game started";
    }
}
