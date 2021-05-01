public class xComNewgameRandom extends xCom {
    public String doCommand(String fullCommand) {
        sSettings.NET_MODE = sSettings.NET_SERVER;
        if(eManager.mapsSelection.length < 1) {
            return "no maps found for new game (random)";
        }
        else if(eManager.mapsSelection.length > 1) {
            int rand = (int)(Math.random()*eManager.mapsSelection.length);
            while(rand == eManager.mapSelectionIndex) {
                rand = (int)(Math.random()*eManager.mapsSelection.length);
            }
            eManager.mapSelectionIndex = rand;
            xCon.ex(String.format("load %s", eManager.mapsSelection[rand]));
        }
        else {
            eManager.mapSelectionIndex = 0;
            xCon.ex(String.format("load %s", eManager.mapsSelection[0]));
        }
        return "new game (random) started";
    }
}
