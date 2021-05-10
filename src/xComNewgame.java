public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        nServer.instance().clientArgsMap.put("server", nServer.instance().getNetVars());
        int toplay = eManager.mapSelectionIndex;
        if(toplay < 0)
            xCon.ex("newgamerandom");
        else
            xCon.ex("exec maps/" + eManager.mapsSelection[toplay]);
        nServer.instance().start();
        sSettings.IS_SERVER = true;
        return "new game started";
    }
}
