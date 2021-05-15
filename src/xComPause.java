import java.awt.*;

public class xComPause extends xCom {
    public String doCommand(String fullCommand) {
        uiInterface.inplay = !uiInterface.inplay;
        oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if(uiInterface.inplay)
            oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
        if(sSettings.show_mapmaker_ui) {
            if(uiInterface.inplay)
                nClient.instance().addNetCmd("respawnnetplayer " + uiInterface.uuid);
            else
                nClient.instance().addNetCmd("deleteplayer " + uiInterface.uuid);
        }
        return fullCommand;
    }
}
