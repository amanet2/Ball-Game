public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        int toplay = eManager.mapSelectionIndex;
        if(toplay < 0)
            xCon.ex("newgamerandom");
        else
            xCon.ex("exec maps/" + eManager.mapsSelection[toplay]);
        xCon.ex("startserver");
        return "new game started";
    }
}
