public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
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
