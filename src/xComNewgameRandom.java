public class xComNewgameRandom extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.mapsFileSelection.length < 1)
            return "no maps found for new game (random)";
        else if(eManager.mapsFileSelection.length > 1) {
            int rand = (int)(Math.random()*eManager.mapsFileSelection.length);
            while(rand == eManager.mapSelectionIndex) {
                rand = (int)(Math.random()*eManager.mapsFileSelection.length);
            }
            eManager.mapSelectionIndex = rand;
            xCon.ex("changemap maps/" + eManager.mapsFileSelection[rand]);
        }
        else {
            eManager.mapSelectionIndex = 0;
            xCon.ex("changemap maps/" + eManager.mapsFileSelection[0]);
        }
        return "new game (random) started";
    }
}
