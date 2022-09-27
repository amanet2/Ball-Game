public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("startserver");
        int toplay = eManager.mapSelectionIndex;
        if(toplay < 0)
            xCon.ex("newgamerandom");
        else
            xCon.ex("changemap maps/" + eManager.mapsFileSelection[toplay]);
        return "new game started";
    }
}
