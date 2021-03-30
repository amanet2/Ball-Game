public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        sSettings.net_server = true;
        sSettings.NET_MODE = sSettings.NET_SERVER;
        int toplay = eManager.mapSelectionIndex;
        if(toplay < 0) {
            xCon.ex("newgamerandom");
        }
        else {
            xCon.ex(String.format("load %s", eManager.mapsSelection[toplay]));
        }
        return "new game started";
    }
}
