import java.util.HashMap;

public class xComPause extends xCom {
    public String doCommand(String fullCommand) {
        uiInterface.inplay = !uiInterface.inplay;
        if(uiInterface.inplay) {
                oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
                xCon.ex("playsound sounds/clampdown.wav");
                if(sSettings.show_mapmaker_ui || !cScripts.isNetworkGame())
                        cScripts.setupGame();
            }
        else {
                xCon.ex("playsound sounds/grenpinpull.wav");
                if(sSettings.show_mapmaker_ui || !cScripts.isNetworkGame()) {
                    eManager.currentMap.scene.players().clear();
                    eManager.currentMap.scene.objectMaps.put("THING_PLAYER", new HashMap<>());
                }
            }
        return fullCommand;
    }
}
