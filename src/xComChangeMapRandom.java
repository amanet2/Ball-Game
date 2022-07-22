public class xComChangeMapRandom extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.mapsFileSelection.length < 1) {
            return "no maps found for changemap (random)";
        }
        else if(eManager.mapsFileSelection.length > 1) {
            int rand = eManager.mapSelectionIndex;
            while(rand == eManager.mapSelectionIndex) {
                rand = (int)(Math.random()*eManager.mapsFileSelection.length);
            }
            cServerLogic.changeMap("maps/" + eManager.mapsFileSelection[rand]);
            eManager.mapSelectionIndex = rand;
        }
        else {
            cServerLogic.changeMap("maps/" + eManager.mapsFileSelection[0]);
            eManager.mapSelectionIndex = 0;
        }
        return "changed map (random)";
    }
}
