public class xComChangeMapRandom extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.mapsSelection.length < 1) {
            return "no maps found for changemap (random)";
        }
        else if(eManager.mapsSelection.length > 1) {
            int rand = (int)(Math.random()*eManager.mapsSelection.length);
            while(rand == eManager.mapSelectionIndex) {
                rand = (int)(Math.random()*eManager.mapsSelection.length);
            }
            nServer.instance().changeMap(eManager.mapsSelection[rand]);
            eManager.mapSelectionIndex = rand;
            cGameLogic.resetGameState();
        }
        else {
            nServer.instance().changeMap(eManager.mapsSelection[0]);
            eManager.mapSelectionIndex = 0;
            cGameLogic.resetGameState();
        }
        return "changed map (random)";
    }
}
